package br.com.knipers.decomposer.groovy;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.syntax.Types;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ScriptRunner {
    private GroovyShell shell;

    public ScriptRunner() {
        SecureASTCustomizer secure = new SecureASTCustomizer();
        secure.setClosuresAllowed(false);
        secure.setMethodDefinitionAllowed(false);

        secure.setImportsWhitelist(Collections.<String>emptyList());
        secure.setStaticImportsWhitelist(Collections.<String>emptyList());
        secure.setStaticStarImportsWhitelist(Arrays.asList("java.lang.Math"));

        List<Integer> allowedTokens = new ArrayList<>();
        allowedTokens.add(Types.PLUS);
        allowedTokens.add(Types.MINUS);
        allowedTokens.add(Types.MULTIPLY);
        allowedTokens.add(Types.DIVIDE);
        allowedTokens.add(Types.EQUAL);
        allowedTokens.add(Types.POWER);
        allowedTokens.add(Types.SEMICOLON);
        secure.setTokensWhitelist(allowedTokens);

        List<Class> constantTypesWhiteList = new ArrayList<>();
        constantTypesWhiteList.add(Integer.class);
        constantTypesWhiteList.add(Float.class);
        constantTypesWhiteList.add(Long.class);
        constantTypesWhiteList.add(Double.class);
        constantTypesWhiteList.add(BigDecimal.class);
        constantTypesWhiteList.add(Integer.TYPE);
        constantTypesWhiteList.add(Long.TYPE);
        constantTypesWhiteList.add(Float.TYPE);
        constantTypesWhiteList.add(Double.TYPE);
        constantTypesWhiteList.add(Object.class);
        secure.setConstantTypesClassesWhiteList(constantTypesWhiteList);

        List<Class> receiversWhitelist = new ArrayList<>();
        receiversWhitelist.add(Math.class);
        receiversWhitelist.add(Integer.class);
        receiversWhitelist.add(Float.class);
        receiversWhitelist.add(Double.class);
        receiversWhitelist.add(Long.class);
        receiversWhitelist.add(BigDecimal.class);
        secure.setReceiversClassesWhiteList(receiversWhitelist);

        List<Class<? extends Statement>> statementsWhitelist = new ArrayList<>();
        statementsWhitelist.add(BlockStatement.class);
        statementsWhitelist.add(ExpressionStatement.class);
        statementsWhitelist.add(ReturnStatement.class);
        secure.setStatementsWhitelist(statementsWhitelist);

        List<Class<? extends Expression>> expressionsWhitelist = new ArrayList<>();
        expressionsWhitelist.add(BinaryExpression.class);
        expressionsWhitelist.add(ConstantExpression.class);
        expressionsWhitelist.add(UnaryMinusExpression.class);
        expressionsWhitelist.add(UnaryPlusExpression.class);
        expressionsWhitelist.add(PrefixExpression.class);
        expressionsWhitelist.add(PostfixExpression.class);
        expressionsWhitelist.add(TernaryExpression.class);
        expressionsWhitelist.add(BooleanExpression.class);
        expressionsWhitelist.add(DeclarationExpression.class);
        expressionsWhitelist.add(VariableExpression.class);
        secure.setExpressionsWhitelist(expressionsWhitelist);

        ImportCustomizer imports = new ImportCustomizer().addStaticStars("java.lang.Math");

        CompilerConfiguration config = new CompilerConfiguration();
        config.addCompilationCustomizers(imports, secure);
        shell = new GroovyShell(config);
    }

    public Object executeScript(Script script, List<Variable> variables) {
        synchronized(shell) {
            for (Variable variable : variables) {
                shell.setProperty(variable.getName(), variable.getValue());
            }
            return script.run();
        }
    }
    public Script parse(String script) {
        return shell.parse(script);
    }
}
