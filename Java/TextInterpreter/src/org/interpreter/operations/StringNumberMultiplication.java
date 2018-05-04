package org.interpreter.operations;

import org.interpreter.Interpreter;
import org.interpreter.variables.StringType;
import org.interpreter.variables.Type;
import org.interpreter.variables.VariableContainer;

public class StringNumberMultiplication extends Interpreter implements Operation {
    VariableContainer variableContainer;

    public StringNumberMultiplication(VariableContainer variableContainer) {
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


        Type currentType = new StringType();
        String current =  "";

        for (int i = 0; i < (int) this.variableContainer.getValue(secondVarToAdd); i++) {
            current = current + (String) this.variableContainer.getValue(firstVarToAdd);
        }

        currentType.setValue(current);

        this.variableContainer.removeEntry(varToOverride);
        this.variableContainer.insertNameTypeValue(varToOverride, currentType, currentType.getValue());
    }
}
