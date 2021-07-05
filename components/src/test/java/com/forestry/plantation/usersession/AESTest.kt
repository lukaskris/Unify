package com.forestry.plantation.usersession

import com.forestry.plantation.usersession.security.AESUtils
import org.junit.Test


/**
 * Created by Lukas Kristianto on 4/29/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class AESTest {
	@Test
	fun `test encrypt and decrypt key`() {
		val key = "sinarmas app"
		var encrypt = AESUtils.encrypt(key)
		var decrypt = AESUtils.decrypt(encrypt)

		assert(encrypt != key)
		assert(decrypt == key)

		encrypt = AESUtils.encrypt(key)
		decrypt = AESUtils.decrypt(encrypt)

		assert(encrypt != key)
		assert(decrypt == key)
	}
}