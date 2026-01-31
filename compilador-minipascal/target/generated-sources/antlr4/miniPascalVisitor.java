// Generated from miniPascal.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link miniPascalParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface miniPascalVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(miniPascalParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(miniPascalParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(miniPascalParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#variableDeclarationPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationPart(miniPascalParser.VariableDeclarationPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(miniPascalParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#identifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierList(miniPascalParser.IdentifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#type_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_(miniPascalParser.Type_Context ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(miniPascalParser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#statements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatements(miniPascalParser.StatementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(miniPascalParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#simpleStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleStatement(miniPascalParser.SimpleStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(miniPascalParser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#ioStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIoStatement(miniPascalParser.IoStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#structuredStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStructuredStatement(miniPascalParser.StructuredStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(miniPascalParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(miniPascalParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#forStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatement(miniPascalParser.ForStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(miniPascalParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#relationalOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalOperator(miniPascalParser.RelationalOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#simpleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleExpression(miniPascalParser.SimpleExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#additiveOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveOperator(miniPascalParser.AdditiveOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(miniPascalParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeOperator(miniPascalParser.MultiplicativeOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(miniPascalParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#unsignedNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsignedNumber(miniPascalParser.UnsignedNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#stringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(miniPascalParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link miniPascalParser#boolLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(miniPascalParser.BoolLiteralContext ctx);
}