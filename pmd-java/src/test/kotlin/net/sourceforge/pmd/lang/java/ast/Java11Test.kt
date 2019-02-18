
import io.kotlintest.shouldBe
import net.sourceforge.pmd.lang.java.ast.*
import net.sourceforge.pmd.lang.java.ast.JavaVersion.*
import net.sourceforge.pmd.lang.java.ast.JavaVersion.Companion.Latest

class Java11Test : ParserTestSpec({

    parserTestGroup("Test lambda parameter with var keyword") {

        // var keyword should be a normal type pre-java 11
        onVersions(J1_8..J10) {

            "(var x) -> String.valueOf(x)" should matchExpr<ASTLambdaExpression> {
                child<ASTFormalParameters> {
                    child<ASTFormalParameter> {
                        child<ASTClassOrInterfaceType> {
                            it.image shouldBe "var"
                        }

                        child<ASTVariableDeclaratorId> { }
                    }
                }

                unspecifiedChild()
            }

            "(var x, var y) -> x + y" should matchExpr<ASTLambdaExpression> {
                child<ASTFormalParameters> {
                    child<ASTFormalParameter> {
                        child<ASTClassOrInterfaceType> {
                            it.image shouldBe "var"
                        }
                        child<ASTVariableDeclaratorId> { }
                    }

                    child<ASTFormalParameter> {
                        child<ASTClassOrInterfaceType> {
                            it.image shouldBe "var"
                        }
                        child<ASTVariableDeclaratorId> { }

                    }
                }

                unspecifiedChild()
            }

            "(@Nonnull var x) -> String.valueOf(x)" should matchExpr<ASTLambdaExpression> {
                child<ASTFormalParameters> {
                    child<ASTFormalParameter> {
                        child<ASTAnnotation>(ignoreChildren = true) {}
                        child<ASTType>(ignoreChildren = true) {}
                        child<ASTVariableDeclaratorId> {}
                    }
                }
                unspecifiedChild()
            }
        }

        // var keyword should generate no type after java 11
        onVersions(J11..Latest) {
            "(var x) -> String.valueOf(x)" should matchExpr<ASTLambdaExpression> {
                child<ASTFormalParameters> {
                    child<ASTFormalParameter> {
                        it.isTypeInferred shouldBe true
                        child<ASTVariableDeclaratorId> { }
                    }
                }

                unspecifiedChild()
            }
        }
    }

})