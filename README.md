# Deterministic Universe v2 — Algorithm Lab

A native, offline Android educational game that visualizes and benchmarks ten deterministic graph algorithms. The same graph, stable ordering, fixed seed and explicit tie-breaking reproduce the same ordered result.

## Version 2 highlights

- Tap-selectable start and destination nodes
- Pinch-to-zoom and drag-to-pan visualization
- Result explanations with complexity and cost
- Nine-column controlled benchmark table
- Technical and functional guide for all ten algorithms

## Included engines

BFS, DFS, Dijkstra, A*, bidirectional Dijkstra, Bellman–Ford, Floyd–Warshall, Kruskal MST, Prim MST and topological sort.

## Build and test

Requirements: JDK 17, Android SDK 35 and Gradle 8.9.

```bash
gradle testDebugUnitTest lintDebug assembleDebug
```

The installable test APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

For a signed Play bundle, create an upload keystore and export:

```bash
export DU_KEYSTORE_FILE=/absolute/path/upload.jks
export DU_KEYSTORE_PASSWORD='your-password'
export DU_KEY_ALIAS='upload'
export DU_KEY_PASSWORD='your-password'
gradle clean testReleaseUnitTest lintRelease bundleRelease
```

Never put signing secrets in this project. The release AAB appears under `app/build/outputs/bundle/release/`.

## Architecture and benchmark integrity

The pure-Java algorithm engine is isolated from Canvas rendering. UI time is not counted. Benchmark mode performs three warm-ups and ten measured executions, then shows the median. All queues use stable node-ID tie-breaking. Graph weights are integer values to avoid floating-point route variation.

## Important scope note

This v1 includes the complete offline engine, visualization, algorithm selector, explanations, deterministic tests and on-device comparison. Store artwork, publisher identity and the private signing key intentionally remain publisher-owned tasks.
