# plugin_discovery platform configuration

**English** | [中文](README.zh-CN.md)

The platform-tier configuration of [plugin_discovery](https://github.com/Whisper-chen/plugin_discovery): for every machine, OS type and domain, which backends Qualcomm fully supports. It lives on the `platform-config` branch, apart from the library, so that it can change and be released without touching the library.

## Layout

```
<domain>/<machine>-<os>-<domain>.config.yaml     e.g. camera/QAM8797P-QCLinux-camera.config.yaml
```

- `<machine>`: the string in `/sys/devices/soc0/machine`. `<os>`: `Ubuntu` or `QCLinux`. Names are compared ignoring case.
- The domain in the name must be the directory it is in, and the file must hold that domain's block. The build fails otherwise.
- Each domain's directory belongs to that domain's team (see `.github/CODEOWNERS`): they add machines and change their backend lists through pull requests.

## Package

One Debian package, `plugin-discovery-platform-config` (architecture `all`), installs every file into `/usr/share/plugin_discovery/platforms.d/`. On the first boot `platform-detect` links the ones that match the machine and OS into `/etc/plugin_discovery/platform.d/`; the package's postinst and postrm run `platform-detect --force` so that installing or removing it refreshes the links.

```bash
cmake -S . -B build && cd build && cpack -G DEB      # plugin-discovery-platform-config_<version>_all.deb
sudo dpkg -i plugin-discovery-platform-config_*_all.deb
pluginlib-detect -d camera                           # the merged result
```

**Qualcomm Linux** has a read-only root file system and an image is built for one machine, so it does not use this package or `platform-detect`: the recipe [`yocto/plugin-discovery-platform-config_0.1.0.bb`](yocto/plugin-discovery-platform-config_0.1.0.bb) installs the candidate of the image's machine directly as `/etc/plugin_discovery/platform.d/<domain>.config.yaml`. The recipe has not been built yet.

Customer configuration is not part of this package: it goes to `/etc/plugin_discovery/config.d/` from the customer's own package.

## License

BSD-3-Clause-Clear, see [`LICENSE`](LICENSE).
