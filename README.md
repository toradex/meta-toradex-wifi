This README file contains information on the contents of the meta-toradex-wifi layer. This layer adds support for the
NXP proprietary wifi drivers that can be enabled instead of the mainline mwifiex drivers. It adds a blacklisting
mechanism for the modules making it simple to choose what module to use based on the modprobe configuration files.

This layer also includes support for enabling the manufacturing mode for the NXP wifi drivers, that can be used
for lab testing.

Please see the corresponding sections below for details.

Supported SOMS:
- colibri-imx6ull: interface-diversity-sd-sd
- colibri-imx8x:   interface-diversity-pcie-usb
- verdin-imx8mm:   interface-diversity-sd-sd
- verdin-imx8mp:   interface-diversity-sd-sd
- apalis-imx8:     interface-diversity-pcie-usb

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

Enable the layer in your build:
```
$ cd ${BUILD_DIR}
$ echo 'BBLAYERS += " ${TOPDIR}/../layers/meta-toradex-wifi"' >> conf/bblayers.conf
$ echo 'INHERIT += "toradex-wifi-nxp-proprietary-driver"' >> conf/auto.conf
```

## Download NXP Archives

This layer does not contain the actual sources and firmware binaries from NXP. These files are available from your support account at https://www.nxp.com/mynxp/library.  Once you are logged in, you can view your library by product and filter for “88w8997”.  Then you need to download the files in the following table:

| Machine | Normal Archive | Manufacturing Mode Archive |
| :--     | :--            | :--                        |
| colibri-imx6ull | SD-WLAN-SD-BT-8997-U16-MMC-W16.68.10.p162-16.26.10.p162-C4X16693_V4-MGPL.zip | MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip |
| verdin-imx8mm   | SD-WLAN-SD-BT-8997-U16-MMC-W16.68.10.p162-16.26.10.p162-C4X16693_V4-MGPL.zip | MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip | 
| apalis-imx8     | PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p70-16.26.10.p70-C4X16672_V4-GPL.zip | MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip |
| colibri-imx8x   | PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p70-16.26.10.p70-C4X16672_V4-GPL.zip | MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip |

Download these files into a known location on your build system and then add the following to your `local.conf` file to tell the build where to find them.

```
NXP_PROPRIETARY_DRIVER_LOCATION = "file:///home/user/wifi-archive"
```

Note the three `/` characters is required in this URI specification.

As NXP updates these drivers from time to time, the versions of the files that are available at any point in time can be different from the ones presented on the table. Download the proper files according to the necessary interface diversity option of your Toradex Module.

## Configure
There are a few configuration options available with this layer. These options are specified in your local.conf or auto.conf file using the MACHINEOVERRIDES variable.

To set the default driver to be used at runtime to the mlan driver, add the following to your config file:

```
MACHINEOVERRIDES =. "default-nxp-proprietary-driver:"
```

To enable manufacturing mode, use the above setting to default to the mlan driver and add the following to your config file:

```
MACHINEOVERRIDES =. "mfg-mode:"
```

Configure the filenames of the downloaded NXP sources with these variables on your `local.conf` file:

```
NXP_PROPRIETARY_DRIVER_FILENAME_interface-diversity-pcie-usb = "PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p173-16.26.10.p173-C4X16698_V4-MGPL.zip"
NXP_PROPRIETARY_DRIVER_SHA256_interface-diversity-pcie-usb="24edfcc985c9c7710c7a8f96de8e8b5ed3037c76126c48a49486f4e517ab5335"
NXP_PROPRIETARY_DRIVER_FILENAME_interface-diversity-sd-sd= "SD-WLAN-SD-BT-8997-U16-MMC-W16.68.10.p162-16.26.10.p162-C4X16693_V4-MGPL.zip"
NXP_PROPRIETARY_DRIVER_SHA256_interface-diversity-sd-sd="37ffc4ffe9030fe294001ab59b0c168cdaa994ba83e83e2b1369fe0846cd62f9"
NXP_PROPRIETARY_DRIVER_FILENAME_interface-diversity-usb-usb= "USB-WLAN-BT-8997-U16-X86-W16.68.10.p136-16.26.10.p136-C4X16687_V4-MGPL.zip"
NXP_PROPRIETARY_DRIVER_SHA256_interface-diversity-usb-usb="d11a5a0bf6aef352a03517192465094175ac7e65d1f9dd663e6129693b0947ff"
NXP_PROPRIETARY_MFG_TOOL_FILENAME="MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip"
NXP_PROPRIETARY_MFG_TOOL_SHA256="599031b9040c3a501f656a30f85308b9a1929ed5d1f7c40f14c370298f8ba8f9"
```

You only need the variables configuring the interface diversity that is needed by your toradex module. You also need to fill in the sha256 hash for
the file you downloaded. You can get in on linux by using:

```
$ sha256sum PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p173-16.26.10.p173-C4X16698_V4-MGPL.zip
24edfcc985c9c7710c7a8f96de8e8b5ed3037c76126c48a49486f4e517ab5335  PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p173-16.26.10.p173-C4X16698_V4-MGPL.zip
```

## Build

With the above setup, your normal bitbake builds should work and the logic in the layer will set everything else up for you.

```
$ bitbake tdx-reference-minimal-image
```

## Runtime

The toradex-wifi-config recipe will install the /etc/modprobe.d/toradex-wifi-config.conf file with contents similar to the following:

```
# blacklist mlan bt8xxx
# install mlan /bin/false
# install bt8xxx /bin/false

blacklist mwifiex mwifiex_sdio btmrvl btmrvl_sdio
install mwifiex /bin/false
install btmrvl /bin/false
```

To switch between drivers, simply comment out one set of entries, and uncomment the other. Then you will need to reboot.

## Manufacturing Mode

If your build has been configured for manufacturing mode, you will have a binary executable named labtool in the /home/root directory.

# Dependencies

  URI: git://git.toradex.com/meta-toradex-bsp-common
  branch: dunfell-5.x.y
  revision: HEAD

  URI: git://git.openembedded.org/bitbake
  branch: dunfell
  revision: HEAD

  URI: git://git.openembedded.org/openembedded-core
  layers: meta
  branch: dunfell
  revision: HEAD

