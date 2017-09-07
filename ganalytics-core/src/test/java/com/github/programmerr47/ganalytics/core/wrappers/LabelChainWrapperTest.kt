package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.NamingConventions.LOWER_SNAKE_CASE
import org.junit.Assert.assertTrue
import org.junit.Test

class LabelChainWrapperTest : SingleWrapperTest() {

    @Test(expected = IllegalStateException::class)
    fun checkErrorForReturningTypeOfMethodIfItAlreadyHasCompleteSetOfEvents() {
        run(FullDataInterface::class) { action("label", 1) }
    }

    @Test(expected = IllegalStateException::class)
    fun checkErrorForReturningTypeOfMethodIfItAlreadyHasAtLeastALabel() {
        run(LabelActionInterface::class) { action("label") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorWhenCallingLabelMethodWithNotNumberArg() {
        run(CategoryInterface::class) { action().labelLabel("more labels") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorWhenCallingLabelMethodWithMoreThatOneArgs() {
        run(CategoryInterface::class) { action().manyArgsLabel(3, "unknown label") }
    }

    @Test(expected = IllegalStateException::class)
    fun checkErrorForReturningTypeOfMethodIfItAnnotatedLabelFun() {
        run(LabelFunInterface::class) { label() }
    }

    @Test
    fun checkSimpleWorkingCase() {
        run(CategoryInterface::class) {
            assertEquals(Event("categoryinterface", "action", "label")) { action().label() }
            assertEquals(Event("categoryinterface", "action", "otherlabel")) { action().otherLabel() }
            assertEquals(Event("categoryinterface", "action", "valuelabel", 3)) { action().valueLabel(3) }
        }
    }

    @Test
    fun checkApplyingConventionToLabelInterfaces() {
        run(CategoryInterfaceWithConvention::class) {
            assertEquals(Event("category_interface", "action", "other_label")) { action().otherLabel() }
            assertEquals(Event("category_interface", "action", "value_label", 3)) { action().valueLabel(3) }
        }
    }

    @Test
    fun checkCachingLabelInterfaceForRepeatingCalls() {
        run(CategoryInterface::class) {
            val labelInterface1 = action()
            val labelInterface2 = action()
            assertTrue(labelInterface1 == labelInterface2)
        }
    }

    @Test
    fun checkIgnoringMostOfAnnotations() {
        run(StrangeInterface::class) {
            assertEquals(Event("strangeinterface", "action", "label")) { action().label() }
            assertEquals(Event("strangeinterface", "action", "otherlabel")) { action().otherLabel() }
            assertEquals(Event("strangeinterface", "action", "noprefixlabel")) { action().noPrefixLabel() }
            assertEquals(Event("strangeinterface", "action", "nopostfixlabel")) { action().noPostfixLabel() }
        }
    }

    interface FullDataInterface {
        fun action(label: String, value: Number): LabelInterface
    }

    interface LabelActionInterface {
        fun action(label: String): LabelInterface
    }

    interface CategoryInterface {
        fun action(): LabelInterface
    }

    @Convention(LOWER_SNAKE_CASE)
    interface CategoryInterfaceWithConvention {
        fun action(): LabelInterface
    }

    interface LabelFunInterface {
        @LabelFun("action")
        fun label(): LabelInterface
    }

    interface StrangeInterface {
        fun action(): LabelInterfaceWithUnusedAnnotations
    }

    interface LabelInterface {
        fun label()
        fun otherLabel()
        fun valueLabel(value: Number)
        fun labelLabel(label: String)
        fun manyArgsLabel(value: Number, unknown: String)
    }

    @Category("catacata")
    @Convention(LOWER_SNAKE_CASE)
    @HasPrefix
    @HasPostfix("postfix")
    interface LabelInterfaceWithUnusedAnnotations {
        @Action("wow, action?!")
        fun label()

        @LabelFun("ok, action", "label?!")
        fun otherLabel()

        @NoPrefix
        fun noPrefixLabel()

        @NoPostfix
        fun noPostfixLabel()
    }
}