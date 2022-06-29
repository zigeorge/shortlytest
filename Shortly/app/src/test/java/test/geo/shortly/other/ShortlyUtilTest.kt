package test.geo.shortly.other

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShortlyUtilTest {

    val caseInvalidLink = "asdfasdfadswf"
    val caseValidLinkWithHttp = "http://asdfsaf.asfsd"
    val caseValidLinkWithHttps = "https://asdfsaf.asfsd"
    val caseInvalidLinkWithHttp = "http://asdfsaf"
    val caseInvalidLinkWithHttps = "https://asdfsaf"
    val caseValidLinkWithoutPrefix = "asfasd.asdfdsa.asdfsaf"
    val caseLinkEndingWithDot = "asfasd."

    @Test
    fun `invalid link should return true`() {
        val result = ShortlyUtil.isInvalidLink(caseInvalidLink)

        assertThat(result).isTrue()
    }

    @Test
    fun `valid link with http should return false`() {
        val result = ShortlyUtil.isInvalidLink(caseValidLinkWithHttp)

        assertThat(result).isFalse()
    }

    @Test
    fun `valid link with https should return false`() {
        val result = ShortlyUtil.isInvalidLink(caseValidLinkWithHttps)

        assertThat(result).isFalse()
    }

    @Test
    fun `invalid link with http should return true`() {
        val result = ShortlyUtil.isInvalidLink(caseInvalidLinkWithHttp)

        assertThat(result).isTrue()
    }

    @Test
    fun `invalid link with https should return true`() {
        val result = ShortlyUtil.isInvalidLink(caseInvalidLinkWithHttps)

        assertThat(result).isTrue()
    }

    @Test
    fun `valid link without prefix should return false`() {
        val result = ShortlyUtil.isInvalidLink(caseValidLinkWithoutPrefix)

        assertThat(result).isFalse()
    }

    @Test
    fun `link with ending with dot should return false`() {
        val result = ShortlyUtil.isInvalidLink(caseLinkEndingWithDot)

        assertThat(result).isFalse()
    }
}