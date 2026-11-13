# Pokedex

A Pokédex for the original 151, built as a **22-module Android app** on
[Arus](https://github.com/renaldi2108/arus) for navigation and state, Koin for injection, and
Material 3 Expressive for the look. Data comes from the PokeAPI GraphQL endpoint and is cached in
Room, so the app opens offline after its first sync.

| Pokédex | Detail | Evolution |
|---|---|---|
| Type-tinted cards, pill search, header that follows the filter | Theme shifts to the Pokémon's type | Chain resolved from the local cache |

Light and dark both follow the system setting.

---

## Architecture

The module graph is the architecture. Nothing enforces layering as well as a build that refuses to
compile, so every inter-module dependency is checked against a matrix on every `check`.

```
app                                  assembles the graph, owns deep links, starts Koin

core/model          pure Kotlin types: Pokemon, PokemonType, PokemonStats, Evolution
core/common         Outcome, formatting, dispatchers — no Android framework
core/domain         repository interfaces and shared use cases
core/database       Room entity, DAO, DataStore favourites, entity mappers
core/data           Apollo client, GraphQL mapper, repository implementations
core/designsystem   theme, 18-type palette, shared components
core/navigation     the AppRoute marker every destination implements
core/fixtures       sample Pokémon for previews and tests
core/testing        fakes and dispatcher rules shared by every module's tests

feature/splash      presentation/{navigation,ui}
feature/pokedex     domain · presentation/{navigation,ui}
feature/detail      domain · presentation/{navigation,ui}
feature/favorite    domain · data · presentation/{navigation,ui}
```

### Rules the build enforces

`./gradlew checkModuleDependencies` compares the real module graph against `ALLOWED_MATRIX` in
`build-logic/src/main/kotlin/ModuleGuardConventionPlugin.kt` and fails on any edge that is not
listed. The rules it encodes:

- **Routes sit below UI.** A feature's `presentation:navigation` module depends only on `core`, so
  every layer above it — including *other features* — can name its destinations.
- **Features never see each other's UI.** Cross-feature dependencies may only point at `:domain`
  or `:presentation:navigation`. `:feature:pokedex:presentation:ui` can push a `DetailRoute`
  without knowing a `DetailScreen` exists.
- **Only a data layer touches storage.** `core/database` and `core/data` are reachable from
  `core/data` and `feature/*/data`. Presentation and domain know repository *interfaces* in
  `core/domain` and nothing else.
- **Nothing depends on `:app`.**

Adding a module means adding it to the matrix first. That is deliberate.

### Why these boundaries pay off

`PokedexController` takes an `ObservePokedex`, a `FilterPokedex`, a `Navigator` and a
`SavedStateHandle`. It holds no `Context`, no Activity and no Compose import, so all 26 of its
tests run on the JVM against fakes with no emulator and no DI container.

---

## Navigation with Arus

Destinations are `@Serializable` types, not strings, so arguments are compiler-checked and nothing
is parsed at navigation time.

```kotlin
@Serializable
data class DetailRoute(val pokemonId: Int) : AppRoute
```

Each feature contributes its screens through an `ArusEntries` extension, and the same declaration
registers the route for saved state — there is no second list to keep in step:

```kotlin
fun ArusEntries.pokedexEntries(snackbar: SnackbarHostState) {
    screen<PokedexRoute> {
        val controller: PokedexController = koinViewModel { parametersOf(navigator) }
        CollectEffects(controller) { effect -> ... }
        PokedexScreen(controller)
    }

    bottomSheetDialog<FilterRoute>(BottomSheetProperties(skipPartiallyExpanded = true)) { route ->
        val controller: FilterController =
            koinViewModel { parametersOf(route.toFilter(), navigator) }
        FilterSheet(controller)
    }
}
```

Controllers are resolved from Koin, not assembled by hand at the call site. Each feature's
`presentation:ui` module declares its own:

```kotlin
val pokedexUiModule = module {
    viewModel { (navigator: Navigator) ->
        PokedexController(get(), get(), get(), get(), navigator, get())
    }
}
```

The two things Koin cannot know — the `Navigator`, which belongs to the graph, and the route
arguments — arrive as parameters; everything else, including the entry's `SavedStateHandle`,
is resolved. Adding a dependency to a controller is a one-line change to its constructor and
its definition, and no change at all to the screen that uses it.

`koinViewModel()` reads `LocalViewModelStoreOwner`, which Arus scopes per back-stack entry, so
two `DetailRoute` entries on the stack get two independent controllers rather than sharing one.

The app module assembles them in one place (`MainActivity.kt`):

```kotlin
val graph = rememberArusGraph(start = SplashRoute) {
    splashEntries(snackbar)
    pokedexEntries(snackbar)
    detailEntries(snackbar)
    favoriteEntries(snackbar)
}
```

**Typed results.** The filter sheet is a `ResultRoute<PokedexFilter>`: the list controller awaits a
value instead of having its state mutated behind its back, and it does not know the destination is
a sheet — that is decided where the screen is declared.

```kotlin
when (val chosen = navigator.pushForResult(FilterRoute.from(current))) {
    is NavResult.Completed -> applyFilter(chosen.value)
    NavResult.Cancelled -> Unit
}
```

**State.** Controllers extend `ArusController<State, Effect>`: one immutable state value, one-shot
effects for snackbars, and `savedState(...)` delegates for the query, sort and type filters so they
survive process death. Screens subscribe to a slice with `select { it.items }`, and the search field
reads an `Ars<String>` so typing does not recompose the grid.

**Deep links** live in the app module, the only place allowed to know every feature's routes. A link
opens a synthetic back stack, so Back from a detail screen lands on the list rather than leaving the
app:

```bash
adb shell am start -a android.intent.action.VIEW -d "pokedex://pokedex/25"
adb shell am start -a android.intent.action.VIEW -d "pokedex://favorites"
```

---

## Design

**Material 3 Expressive** with colour driven by the Pokémon's type.

- **Typography** — `displayLarge` at `FontWeight.Black`, 46sp, tight tracking. Labels are bold and
  small; body text stays at Material defaults so descriptions remain readable.
- **Shape** — 24dp cards, 28dp search pill, 32dp bottom sheet, fully rounded chips.
- **Colour** — one seed per type in `PokemonTypePalette`. `container`, `onContainer` and `accent` are
  derived by interpolating the seed toward white or black depending on the theme, and `onSeed` is
  chosen from the seed's luminance, so contrast holds for all 18 types without a hand-maintained
  table. The detail screen shifts its whole gradient to the type.
- **Light and dark** — the theme follows the system setting through `isSystemInDarkTheme()`.
  Both schemes are hand-tuned, and every type palette is derived per mode, so a Fire card is a pale
  salmon in light and a deep ember in dark.
- **A header that answers the filter** — with no type filter the Pokédex header sits on the plain
  background. Pick one or more types and it fades to those types' blended container colour, so the
  screen tells you what you are looking at before you read a chip. The blend is an equal-weight
  average, so Fire + Water lands between the two rather than favouring whichever was tapped first.
- **System bars are part of the screen** — the app is edge-to-edge, and both bars take the screen's
  colour rather than sitting in a grey tray. The status bar is painted by the content behind it;
  the navigation bar gets an explicit `NavigationBarScrim` in the same colour, so a Grass Pokémon
  frames the screen in green top *and* bottom. On the list that colour is the active filter tint,
  which means it fades back to the plain background when the filter is cleared. Android's own
  translucent tray is switched off with `isNavigationBarContrastEnforced = false`, otherwise it
  washes the colour out. `SystemBarIcons` then flips both sets of icons from that colour's
  luminance: dark icons on Electric's yellow, light icons on Fire's red.
- **Motion** — the splash pokeball spins; the detail artwork floats and breathes while its pokeball
  backdrop turns slowly behind it. Stat bars animate from zero on first draw.

---

## Stack

| Concern | Choice |
|---|---|
| Build | AGP 9.3.1 with built-in Kotlin 2.4.21, Gradle 9.8.2, JDK 17 |
| UI | Compose BOM 2026.09.00, Material 3 |
| Navigation & state | Arus 0.1.0 (Navigation 3 under the hood) |
| DI | Koin 4.2.2 |
| Network | Apollo 5.2.0 against `graphql.pokeapi.co/v1beta2` |
| Cache | Room 2.8.5 (KSP 2.3.13), DataStore for favourites |
| Images | Coil 2.7.0 |
| Tests | JUnit 4, Turbine, `arus-testing`, JaCoCo 0.8.13 |

> AGP 9 ships Kotlin itself — the `org.jetbrains.kotlin.android` plugin must **not** be applied, and
> `kapt` is unsupported. Room therefore uses KSP.

---

## Build and run

```bash
./gradlew assembleDebug                 # build
./gradlew :app:installDebug             # install on a connected device
./gradlew testDebugUnitTest             # unit tests
./gradlew coverageVerify                # tests + coverage gate
./gradlew checkModuleDependencies       # layering guard
```

Refreshing the GraphQL schema after an API change:

```bash
./gradlew :core:data:downloadPokeapiApolloSchemaFromIntrospection
```

---

## Tests and coverage

**201 tests across 21 modules, and a gate at 100% line coverage.**

```
LINE        100.0%   <- coverageVerify fails the build below this
INSTRUCTION  97.5%
BRANCH       99.5%
```

`./gradlew coverageVerify` aggregates every module and fails if line coverage drops below 100%.
The HTML report lands in `build/reports/jacoco/coverageReport/html/index.html`.

**Why the gate is lines, not instructions.** JaCoCo cannot reach 100% instruction or branch coverage
on Kotlin coroutine code: the compiler emits resume branches in suspend state machines that execute
only if the coroutine actually suspends at that exact point, and some are unreachable by
construction. Gating on lines is the strongest honest bar; the other two numbers are reported, not
enforced.

**Covering suspend paths.** A `MutableStateFlow` never completes and `flowOf` never suspends, so
neither exercises the resume path. Fakes that need to cover it emit and then yield:

```kotlin
override fun observeAll(): Flow<List<Pokemon>> = flow {
    emit(all)
    yield()
}
```

**What is excluded**, in `build-logic/src/main/kotlin/id/renaldi/pokedex/buildlogic/Coverage.kt`:
Compose UI (`*Screen*`, `*Sheet*`, `*Components*`, `*Theme*`, `*Entries*`), generated code
(Room `*_Impl`, Apollo, kotlinx-serialization), DI wiring (`di/`), and Android framework glue
(`*Activity*`, `SystemBars*`). Everything that holds a decision — controllers, use cases,
repositories, mappers, routes, formatting — is covered.

---

## Conventions

- **No comments in source.** Names and structure carry the meaning; this README carries the rest.
- Convention plugins live in `build-logic/`: `pokedex.android.library`, `pokedex.arus`,
  `pokedex.feature.{ui,domain,navigation}`. A module's build file should be a plugin id, a
  namespace, and its dependencies — nothing else.
- Tests are named as sentences describing behaviour, not method names.
