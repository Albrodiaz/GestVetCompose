package com.albrodiaz.gestvet.core.extensions

import androidx.compose.foundation.lazy.LazyListState

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemScrollOffset == 0