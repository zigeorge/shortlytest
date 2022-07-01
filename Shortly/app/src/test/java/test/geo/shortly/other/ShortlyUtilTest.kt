package test.geo.shortly.other

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ShortlyUtilTest {

    private val caseInvalidLink = "asdfasdfadswf"
    private val caseValidLinkWithHttp = "http://asdfsaf.asfsd"
    private val caseValidLinkWithHttps = "https://asdfsaf.asfsd"
    private val caseInvalidLinkWithHttp = "http://asdfsaf"
    private val caseInvalidLinkWithHttps = "https://asdfsaf"
    private val caseValidLinkWithoutPrefix = "asfasd.asdfdsa.asdfsaf"
    private val caseLinkEndingWithDot = "asfasd."
    private val caseInvalidInputWithSpace = "asfasd aa asdf"
    private val caseValidInputWithSpace = "asfasd. aa. asdf"

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

    @Test
    fun `invalid input with space should return true`() {
        val result = ShortlyUtil.isInvalidLink(caseInvalidInputWithSpace)

        assertThat(result).isTrue()
    }

    @Test
    fun `valid input with space should return false`() {
        val result = ShortlyUtil.isInvalidLink(caseValidInputWithSpace)

        assertThat(result).isFalse()
    }
}