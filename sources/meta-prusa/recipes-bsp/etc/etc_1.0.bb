LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-3.0;md5=c79ff39f19dfec6d293b95dea7b07891"
SRC_URI = " \
	file://20-etc.preset			\
	file://10-wlan.network			\
	file://20-eth.network			\
	file://bashrc				\
	file://profile				\
	file://wpa_supplicant-wlan0.conf	\
	file://sshd_config	\
	file://usr-share-factory-defaults.mount \
"

CONFFILES = "							\
	${sysconfdir}/profile					\
	${sysconfdir}/bash/bashrc				\
	${sysconfdir}/systemd/network/10-wlan.network		\
	${sysconfdir}/systemd/network/20-eth.network		\
	${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf	\
	${sysconfdir}/ssh/sshd_config	\
"

FILES_${PN} = "							\
	${sysconfdir}/profile					\
	${sysconfdir}/bash/bashrc				\
	${sysconfdir}/systemd/network/10-wlan.network		\
	${sysconfdir}/systemd/network/20-eth.network		\
	${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf	\
	${systemd_unitdir}/system-preset/20-etc.preset		\
	${sysconfdir}/ssh/sshd_config	\
	${systemd_unitdir}/system/usr-share-factory-defaults.mount \
	${systemd_unitdir}/system/local-fs.target.wants/usr-share-factory-defaults.mount \
	/usr/share/factory/defaults \
"

do_install() {
	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/profile			${D}${sysconfdir}/
	install -d ${D}${sysconfdir}/bash
	install -m 644 ${WORKDIR}/bashrc			${D}${sysconfdir}/bash/
	install -d ${D}${sysconfdir}/systemd/network
	install -m 644 ${WORKDIR}/10-wlan.network		${D}${sysconfdir}/systemd/network/
	install -m 644 ${WORKDIR}/20-eth.network		${D}${sysconfdir}/systemd/network/
	install -d ${D}${sysconfdir}/wpa_supplicant
	install -m 644 ${WORKDIR}/wpa_supplicant-wlan0.conf	${D}${sysconfdir}/wpa_supplicant/
	install -d ${D}${systemd_unitdir}/system-preset
	install -m 644 ${WORKDIR}/20-etc.preset			${D}${systemd_unitdir}/system-preset/
	install -d ${D}${sysconfdir}/ssh
	install -m 644 ${WORKDIR}/sshd_config			${D}${sysconfdir}/ssh/
	install -d ${D}${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/usr-share-factory-defaults.mount	${D}${systemd_unitdir}/system
	install -d ${D}${systemd_unitdir}/system/local-fs.target.wants
	ln -s ${systemd_unitdir}/system/usr-share-factory-defaults.mount ${D}${systemd_unitdir}/system/local-fs.target.wants/usr-share-factory-defaults.mount
	install -d ${D}/usr/share/factory/defaults
}

PACKAGE_WRITE_DEPS_append = " systemd-systemctl-native"

pkg_postinst_${PN} () {
	sed 's,/root:/bin/sh,/root:/bin/bash,' -i $D${sysconfdir}/passwd
}
