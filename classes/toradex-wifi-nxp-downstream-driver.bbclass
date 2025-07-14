#
# Settings for using the NXP downstream driver with Toradex BSP builds.
#
# Usage:
#   Inherit this class
#   Add overrides to add features:
#       MACHINEOVERRIDES =. "mfg-mode:"
#       MACHINEOVERRIDES =. "default-nxp-downstream-driver:"
#

# Some modules don't have a special firmware for manufacturing mode
# Take this into account here
MACHINEOVERRIDES:prepend:colibri-imx6ull = "sd-sd:mfgmode-fw:"
MACHINEOVERRIDES:prepend:verdin-imx8mm = "sd-sd:mfgmode-fw:"
MACHINEOVERRIDES:prepend:verdin-imx8mp = "sd-uart:mfgmode-fw:"
MACHINEOVERRIDES:prepend:verdin-am62 = "sd-uart:"
MACHINEOVERRIDES:prepend:apalis-imx8 = "pcie-usb:mfgmode-fw:"
MACHINEOVERRIDES:prepend:colibri-imx8x = "pcie-usb:mfgmode-fw:"

MACHINE_EXTRA_RDEPENDS:append:sd-uart = " \
    kernel-module-mlan \
    kernel-module-moal \
    kernel-module-wifimrvl \
    nxp-wifi-bt-firmware \
    toradex-wifi-config \
"

MACHINE_EXTRA_RDEPENDS:append:sd-sd = " \
    kernel-module-mlan \
    kernel-module-btmrvl \
    kernel-module-btxxx \
    kernel-module-sdxxx \
    kernel-module-wifimrvl \
    nxp-wifi-bt-firmware \
    toradex-wifi-config \
"

MACHINE_EXTRA_RDEPENDS:append:pcie-usb = " \
    kernel-module-mlan \
    kernel-module-pciexxx \
    kernel-module-btxxx \
    kernel-module-wifimrvl \
    kernel-module-btmrvl \
    nxp-wifi-bt-firmware \
    toradex-wifi-config \
"

FIRMWARE_BIN:colibri-imx6ull = "sdsd8997_combo_v4.bin"
FIRMWARE_BIN:verdin-imx8mm= "sdsd8997_combo_v4.bin"
FIRMWARE_BIN:verdin-imx8mp = "sdiouart8997_combo_v4.bin"
FIRMWARE_BIN:verdin-am62 = "sdiouartiw416_combo_v0.bin"
FIRMWARE_BIN:apalis-imx8 = "pcieusb8997_combo_v4.bin"
FIRMWARE_BIN:colibri-imx8x = "pcieusb8997_combo_v4.bin"

FIRMWARE_BIN_MFGMODE:colibri-imx6ull = "sdio8997_sdio_combo.bin"
FIRMWARE_BIN_MFGMODE:verdin-imx8mm = "sdio8997_sdio_combo.bin"
FIRMWARE_BIN_MFGMODE:verdin-imx8mp = "sdio8997_uart_combo.bin"
FIRMWARE_BIN_MFGMODE:verdin-am62 = "sdiouartiw416_combo_v0.bin"
FIRMWARE_BIN_MFGMODE:apalis-imx8 = "pcie8997_usb_combo.bin"
FIRMWARE_BIN_MFGMODE:colibri-imx8x = "pcie8997_usb_combo.bin"

IMAGE_INSTALL:append:mfg-mode = " labtool "

addhandler toradex_wifi_sanity_handler
toradex_wifi_sanity_handler[eventmask] = "bb.event.ParseCompleted"
python toradex_wifi_sanity_handler() {
  if "mfg-mode:" in d.getVar('OVERRIDES') and "default-nxp-downstream-driver:" not in d.getVar('OVERRIDES'):
    bb.fatal("Building for Wi-Fi manufacturing mode requires using the NXP downstream driver as the default.")
}

