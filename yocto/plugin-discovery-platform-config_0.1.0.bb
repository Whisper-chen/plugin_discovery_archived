# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Qualcomm Linux: the root file system is read-only and an image is built for one machine, so the
# platform configuration is chosen here, at build time, and installed as the platform tier:
#   /etc/plugin_discovery/platform.d/<domain>.config.yaml
# platform-detect is not used. NOT BUILT YET: written from the layout of this repository; check it
# in a real QLI build. PD_MACHINE must be the string in /sys/devices/soc0/machine of the device.
SUMMARY = "plugin_discovery platform configuration (Qualcomm Linux)"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE;md5=27bed425d409914cf1a2a322089c7558"
SRC_URI = "git://github.com/qualcomm-qrb-ros/plugin_discovery.git;protocol=https;branch=platform-config"
SRCREV = "<commit of the platform-config branch>"
S = "${WORKDIR}/git"

inherit allarch

PD_MACHINE ?= "QAM8797P"
PD_OS = "QCLinux"
PD_DOMAINS ?= "camera"

do_install() {
    install -d ${D}${sysconfdir}/plugin_discovery/platform.d
    for domain in ${PD_DOMAINS}; do
        install -m 0644 ${S}/${domain}/${PD_MACHINE}-${PD_OS}-${domain}.config.yaml \
            ${D}${sysconfdir}/plugin_discovery/platform.d/${domain}.config.yaml
    done
}
FILES:${PN} = "${sysconfdir}/plugin_discovery"
RDEPENDS:${PN} = "plugin-discovery"
