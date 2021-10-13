SUMMARY = "Kernel firmware for NXP Bluetooth chip"
LICENSE = "CLOSED"

FIRMWARE_BIN_interface-diversity-sd-sd = "sdsd8997_combo_v4.bin"
FIRMWARE_BIN_interface-diversity-sd-sd_mfg-mode = "sdio8997_sdio_combo.bin"
FIRMWARE_BIN_interface-diversity-pcie-usb = "pcieusb8997_combo_v4.bin"
FIRMWARE_BIN_interface-diversity-pcie-usb_mfg-mode = "pcie8997_usb_combo.bin"
FIRMWARE_BIN_interface-diversity-usb-usb = "usbusb8997_combo_v4.bin"
FIRMWARE_BIN_interface-diversity-usb-usb_mfg-mode = "usb8997_usb_combo.bin"

FILES_${PN} = "${base_libdir}/firmware/nxp"

# firmware binaries are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${base_libdir}/firmware/nxp
    install -m 0644 ${S}/${FIRMWARE_BIN} ${D}${base_libdir}/firmware/nxp
}

COMPATIBLE_MACHINE = "(colibri-imx6ull|colibri-imx8x|verdin-imx8mm|verdin-imx8mp|apalis-imx8|apalis-imx8x)"

addtask nxp_driver_unpack before do_patch after do_unpack
do_nxp_driver_unpack() {
    :
}

NXP_DRIVER_PACKAGE_interface-diversity-sd-sd="SD-WLAN-SD-BT-8997-U16-MMC-W16.68.10.p162-16.26.10.p162-C4X16693_V4-MGPL.zip;name=sd-sd-driver"
NXP_DRIVER_PACKAGE_interface-diversity-sd-sd_mfg-mode="MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip;name=sd-sd-mfg-driver"
SRC_URI_append_interface-diversity-sd-sd = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_DRIVER_PACKAGE};subdir=archive.sd-sd "
SRC_URI[sd-sd-driver.sha256sum] = "37ffc4ffe9030fe294001ab59b0c168cdaa994ba83e83e2b1369fe0846cd62f9"
SRC_URI[sd-sd-mfg-driver.sha256sum] = "599031b9040c3a501f656a30f85308b9a1929ed5d1f7c40f14c370298f8ba8f9"
do_nxp_driver_unpack_interface-diversity-sd-sd() {
    tar -C ${S} \
        --strip-components=1 \
        -xf ${WORKDIR}/archive.sd-sd/SD-WLAN-SD-BT-8997-U16-MMC-W16.68.10.p162-16.26.10.p162-C4X16693_V4-MGPL.tar \
        FwImage/${FIRMWARE_BIN}
}
do_nxp_driver_unpack_interface-diversity-sd-sd_mfg-mode() {
    install -m 0644 ${WORKDIR}/archive.sd-sd/MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164/bin/FwImage/${FIRMWARE_BIN} ${S}/${FIRMWARE_BIN}
}

NXP_DRIVER_PACKAGE_interface-diversity-usb-usb="USB-WLAN-BT-8997-U16-X86-W16.68.10.p136-16.26.10.p136-C4X16687_V4-MGPL.zip;name=usb-usb-driver"
NXP_DRIVER_PACKAGE_interface-diversity-usb-usb_mfg-mode="MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip;name=usb-usb-mfg-driver"
SRC_URI_append_interface-diversity-usb-usb = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_DRIVER_PACKAGE};subdir=archive.usb-usb "
SRC_URI[usb-usb-driver.sha256sum] = "07cf67576a1f6f14e63c5a0290b108a0b57863192c59b52c559cc24c38bd35a5"
SRC_URI[usb-usb-mfg-driver.sha256sum] = "599031b9040c3a501f656a30f85308b9a1929ed5d1f7c40f14c370298f8ba8f9"
do_nxp_driver_unpack_interface-diversity-usb-usb() {
    tar -C ${S} \
        --strip-components=1 \
        -xf ${WORKDIR}/archive.usb-usb/USB-WLAN-BT-8997-U16-X86-W16.68.10.p136-16.26.10.p136-C4X16687_V4-MGPL.tar \
        FwImage/${FIRMWARE_BIN}
}
do_nxp_driver_unpack_interface-diversity-usb-usb_mfg-mode() {
    install -m 0644 ${WORKDIR}/archive.usb-usb/MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164/bin/FwImage/${FIRMWARE_BIN} ${S}/${FIRMWARE_BIN}
}

NXP_DRIVER_PACKAGE_interface-diversity-pcie-usb="PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p70-16.26.10.p70-C4X16672_V4-GPL.zip;name=pcie-usb-driver"
NXP_DRIVER_PACKAGE_interface-diversity-pcie-usb_mfg-mode="MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164.zip;name=pcie-usb-mfg-driver"
SRC_URI_append_interface-diversity-pcie-usb = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_DRIVER_PACKAGE};subdir=archive.pcie-usb "
SRC_URI[pcie-usb-driver.sha256sum] = "9c56bffc33e134d3f7502fdf12ee9b0c6b8f9a12c4ef73f6dd0c349384375b4f"
SRC_URI[pcie-usb-mfg-driver.sha256sum] = "599031b9040c3a501f656a30f85308b9a1929ed5d1f7c40f14c370298f8ba8f9"
do_nxp_driver_unpack_interface-diversity-pcie-usb() {
    tar -C ${S} \
        --strip-components=1 \
        -xf ${WORKDIR}/archive.pcie-usb/PCIE-WLAN-USB-BT-8997-U16-X86-W16.88.10.p70-16.26.10.p70-C4X16672_V4-GPL.tar \
        FwImage/${FIRMWARE_BIN}
}
do_nxp_driver_unpack_interface-diversity-pcie-usb_mfg-mode() {
    install -m 0644 ${WORKDIR}/archive.pcie-usb/MFG-W8997-MF-LABTOOL-U14-1.1.0.164-A1-16.80.205.p164/bin/FwImage/${FIRMWARE_BIN} ${S}/${FIRMWARE_BIN}
}
 