This layer adds support for the NXP downstream Wi-Fi drivers that can be enabled instead of the mainline mwifiex drivers.
It adds a blacklisting mechanism for the modules making it simple to choose what module to 
use based on the modprobe configuration files.

This layer also includes support for enabling the manufacturing mode for the NXP Wi-Fi drivers, that can be used
for lab testing and it depends on downstream files that come from NXP.

Toradex makes this files available to customers under NDA, please contact your TSE or FAE for information on how to get access
to the necessary files.

Please see the corresponding sections below for details.

Supported SOMS:
- apalis-imx8:     pcie-usb
- colibri-imx6ull: sd-sd
- colibri-imx8x:   pcie-usb
- verdin-imx8mm:   sd-sd
- verdin-imx8mp:   sd-uart
- verdin-am62:     sd-uart

# How to install

## Setup layer
The first step is to clone this layer in your own yocto layers directory. This can be done manually or by using repo:

```
$ export LAYER_DIR=<full-path-to-your-toradex-bsp-layer-directory>
$ export BUILD_DIR=<full-path-to-your-toradex-bsp-build-directory>
$ export TORADEX_WIFI_BRANCH=<selected-wifi-branch>
$ cd ${LAYER_DIR}
$ mkdir ../.repo/local_manifests
$ cat > ../.repo/local_manifests/toradex-wifi.xml << EOF
<?xml version="1.0" encoding="UTF-8" ?>
<manifest>
<remote fetch="https://github.com/toradex/" name="toradex-wifi"/>
<project name="meta-toradex-wifi" remote="toradex-wifi" revision="$TORADEX_WIFI_BRANCH" path="layers/meta-toradex-wifi"/>
</manifest>
EOF
$ repo sync meta-toradex-wifi
```

Copy the downloaded package from Toradex into your yocto downloads directory (`$DL_DIR` in `local.conf`).

## Enable the layer in your build:

```
# source your yocto environment file
$ . export
$ cd $BBPATH/layers/meta-toradex-wifi
$ ./install_layer.sh <nxp_driver_package_path_and_name>
```

The `install_layer.sh` script will setup all the necessary variables to enable the layer and to build and enable the
downstream drivers by default. Consult this script if you want to know the details of this operation.

## Configure
There are a few configuration options available with this layer. These options are specified in your local.conf or auto.conf file using the MACHINEOVERRIDES variable.

To set the default driver to be used at runtime to the mlan driver, add the following to your config file:

```
MACHINEOVERRIDES =. "default-nxp-downstream-driver:"
```

To enable manufacturing mode, use the above setting to default to the mlan driver and add the following to your config file:

```
MACHINEOVERRIDES =. "mfg-mode:"
```

## Build

With the above setup, your normal bitbake builds should work and the logic in the layer will set everything else up for you.

```
$ bitbake tdx-reference-minimal-image
```

## Runtime

The toradex-wifi-config recipe will install the /etc/modprobe.d/toradex-wifi-config.conf file with contents similar to the following:

```
# blacklist mlan btxxx
# install mlan /bin/false
# install btxxx /bin/false

blacklist mwifiex mwifiex_sdio btmrvl btmrvl_sdio
install mwifiex /bin/false
install btmrvl /bin/false
```

To switch between drivers, simply comment out one set of entries, and uncomment the other. Then you will need to reboot.

## Manufacturing Mode

If your build has been configured for manufacturing mode, you will have a binary executable named labtool in the /root directory.

# Dependencies

  URI: git://git.toradex.com/meta-toradex-bsp-common
  branch: scarthgap-7.x.y
  revision: HEAD

  URI: git://git.openembedded.org/bitbake
  branch: scarthgap
  revision: HEAD

  URI: git://git.openembedded.org/openembedded-core
  layers: meta
  branch: scarthgap
  revision: HEAD

