package org.interpreter.operations;

import org.interpreter.Interpreter;
import org.interpreter.variables.NumberType;
import org.interpreter.variables.StringType;
import org.interpreter.variables.Type;
import org.interpreter.variables.VariableContainer;

public class NumberNumberAddition extends Interpreter implements Operation {
    VariableContainer variableContainer;

    public NumberNumberAddition(VariableContainer variableContainer) {
        this.variableContainer = variableContainer;
    }

    public void execute(String input) {
        String varToOverride = firstWord(input);
        input = removeFirstWord(input);

        String firstVarToAdd = firstWord(input);
        input = removeFirstWord(input);

        input = removeFirstWord(input);//remove the operator

        String secondVarToAdd = firstWord(input);
        input = removeFirstWord(input);

        Type currentType = new NumberType();
        Integer current = (Integer) this.variableContainer.getValue(firstVarToAdd) + (Integer) this.variableContainer.getValue(secondVarToAdd);

        this.variableContainer.removeEntry(varToOverride);
        this.variableContainer.insertNameTypeValue(varToOverride, currentType, current);
    }
}
