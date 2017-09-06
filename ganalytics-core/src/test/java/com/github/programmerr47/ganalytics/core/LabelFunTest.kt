package com.github.programmerr47.ganalytics.core

import com.github.programmerr47.ganalytics.core.NamingConventions.LOWER_SNAKE_CASE
import org.junit.Test

class LabelFunTest : SingleWrapperTest() {

    @Test
    fun checkSimpleLabelFunTest() {
        run(SimpleLabelFunInterface::class) {
            assertEquals(Event("simplelabelfuninterface", "action", "label")) { label() }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorOnPassingTwoOrMoreParameters() {
        run(TwoArgLabelFunInterface::class) { label("sample", 0) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorOnPassingOneNotNumberArg() {
        run(OneNotNumberArgInterface::class) { label("sample") }
    }

    @Test
    fun checkPrefixWithLabelFun() {
        run(LabelFunPrefixInterface::class) {
            assertEquals(Event("cat", "cataction", "simplelabel")) { simpleLabel() }
        }
    }

    @Test
    fun checkApplyingConventionOnLabelFun() {
        run(LabelFunConventionInterface::class) {
            assertEquals(Event("label_fun_convention_interface", "simpleaction", "simple_label")) { simpleLabel() }
        }
    }

    @Test
    fun checkSpecificLabelInLabelFun() {
        run(SpecificLabelFunInterface::class) {
            assertEquals(Event("specificlabelfuninterface", "action", "label")) { otherNameForLabel() }
        }
    }

    @Test(expected = IllegalStateException::class)
    fun checkErrorOnMixingActionAndLabelFun() {
        run(MixingActionAndLabelFunInteface::class) { label() }
    }

    interface SimpleLabelFunInterface {
        @LabelFun("action") fun label()
    }

    interface TwoArgLabelFunInterface {
        @LabelFun("action") fun label(arg1: String, arg2: Number)
    }

    interface OneNotNumberArgInterface {
        @LabelFun("action") fun label(arg: String)
    }

    @HasPrefix
    @Category("cat")
    interface LabelFunPrefixInterface {
        @LabelFun("action") fun simpleLabel()
    }

    @Convention(LOWER_SNAKE_CASE)
    interface LabelFunConventionInterface {
        @LabelFun("simpleaction") fun simpleLabel()
    }

    interface SpecificLabelFunInterface {
        @LabelFun("action", "label") fun otherNameForLabel()
    }

    interface MixingActionAndLabelFunInteface {
        @Action("cool_action") @LabelFun("cool_action_2") fun label()
    }
}