# VPNSDK

A multi-protocol VPN SDK for Android, distributed as a closed-source library
with offline license tokens.

## Layout

```
VPNSDK/
├── TunnelKitLib/         # SDK facade (will be renamed vpnsdk-core in Phase 2)
├── app/                  # sample/demo app
└── protocols/            # VPN protocol forks (git submodules)
    ├── openvpnlib/       → github.com/Fa-d/vpnsdk-openvpnlib
    ├── wgtunlib/         → github.com/Fa-d/vpnsdk-wgtunlib
    ├── singbox/          → github.com/Fa-d/vpnsdk-singbox
    └── strongswan/       → github.com/Fa-d/vpnsdk-strongswan
```

## Cloning

Each protocol module is a git submodule. Clone with `--recursive` so they come
along on first checkout:

```bash
git clone --recursive https://github.com/Fa-d/VPNSDK.git
```

If you already cloned without `--recursive`:

```bash
git submodule update --init --recursive
```

## Pulling upstream protocol updates

Each protocol fork tracks its upstream independently. To pull the latest commits
from every protocol fork in one go:

```bash
git submodule update --remote --merge
```

This advances every submodule pointer to the tip of its tracked branch (`main`)
and merges into the working tree. Commit the resulting pointer bumps from the
parent repo to lock them in.

## Building

```bash
./gradlew :app:assembleDebug
```
