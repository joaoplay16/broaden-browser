package com.playlab.broadenbrowser.utils

import com.google.common.truth.Truth.assertThat
import com.playlab.broadenbrowser.ui.utils.Util.isDateToday
import org.junit.Test

class TestUtil {

    @Test
    fun `timestamp is date today`() {
        val timestamp = System.currentTimeMillis()

        assertThat(isDateToday(timestamp)).isTrue()
    }
}