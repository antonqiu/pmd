package net.sourceforge.pmd.lang.java.ast

import net.sourceforge.pmd.lang.ast.test.shouldBe
import net.sourceforge.pmd.lang.java.ast.ASTPrimitiveType.PrimitiveType.INT
import net.sourceforge.pmd.lang.java.ast.ParserTestCtx.Companion.ExpressionParsingCtx

/**
 * @author Clément Fournier
 * @since 7.0.0
 */
class ASTClassLiteralTest : ParserTestSpec({

    parserTest("Class literals") {

        inContext(ExpressionParsingCtx) {


            "void.class" should parseAs {
                classLiteral { null }
            }


            "int.class" should parseAs {
                classLiteral {
                    primitiveType(INT)
                }
            }

            "Integer.class" should parseAs {
                classLiteral {
                    classType("Integer")
                }
            }


            "int[].class" should parseAs {
                classLiteral {
                    arrayType {
                        it::getElementType shouldBe primitiveType(INT)
                        it::getDimensions shouldBe child {
                            it::getSize shouldBe 1
                            arrayDim()
                        }
                    }
                }
            }


            "List<?>.class" shouldNot parse()
            "Map<String, String>.class" shouldNot parse()
            "java.util.List.class" should parse()
            "java.util.@F List.class" shouldNot parse()

        }


    }

})