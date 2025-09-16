package com.kira.learning.modules.coding

import androidx.lifecycle.SavedStateHandle
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.ViewEvent
import com.kira.learning.base.mvi.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class CodingViewState(
    val code: String = getDefaultCode(ProgrammingLanguage.PYTHON),
    val selectedLanguage: ProgrammingLanguage = ProgrammingLanguage.PYTHON,
    val executionResult: BaseUiState<CodeExecutionResponse> = BaseUiState.Idle,
    val userInput: String = "",
    val showLineNumbers: Boolean = true,
    val isExecuting: Boolean = false
) : ViewState()

sealed interface CodingEvent : ViewEvent {
    data class UpdateCode(val code: String) : CodingEvent
    data class ChangeLanguage(val language: ProgrammingLanguage) : CodingEvent
    data class UpdateUserInput(val input: String) : CodingEvent
    object ExecuteCode : CodingEvent
    object ToggleLineNumbers : CodingEvent
    object ClearResult : CodingEvent
}

@HiltViewModel
class CodingViewModel @Inject constructor(
    private val repository: CodingRepository,
    @Suppress("UNUSED_PARAMETER") private val savedStateHandle: SavedStateHandle
) : BaseViewModel<CodingViewState, CodingEvent>(
    initialState = CodingViewState()
) {

    override fun handleAction(action: CodingEvent) {
        when (action) {
            is CodingEvent.UpdateCode -> updateCode(action.code)
            is CodingEvent.ChangeLanguage -> changeLanguage(action.language)
            is CodingEvent.UpdateUserInput -> updateUserInput(action.input)
            is CodingEvent.ExecuteCode -> executeCode()
            is CodingEvent.ToggleLineNumbers -> toggleLineNumbers()
            is CodingEvent.ClearResult -> clearResult()
        }
    }

    override fun updateLoadingState(isLoading: Boolean, message: String) {
        updateState {
            copy(
                isExecuting = isLoading,
                executionResult = if (isLoading) BaseUiState.Loading else executionResult
            )
        }
    }

    private fun updateCode(code: String) {
        updateState { copy(code = code) }
    }

    private fun changeLanguage(language: ProgrammingLanguage) {
        updateState {
            copy(
                selectedLanguage = language,
                code = getDefaultCode(language),
                executionResult = BaseUiState.Idle
            )
        }
    }

    private fun updateUserInput(input: String) {
        updateState { copy(userInput = input) }
    }

    private fun executeCode() {
        if (currentState.isExecuting) return

        val request = CodeExecutionRequest(
            code = currentState.code,
            language = currentState.selectedLanguage.displayName,
            input = currentState.userInput
        )

        executeApiCall(
            apiCall = { repository.executeCode(request) },
            onSuccess = { response ->
                updateState {
                    copy(executionResult = BaseUiState.Success(response))
                }
            },
            onError = { error ->
                updateState {
                    copy(executionResult = BaseUiState.Error(error.message))
                }
            }
        )
    }

    private fun toggleLineNumbers() {
        updateState { copy(showLineNumbers = !showLineNumbers) }
    }

    private fun clearResult() {
        updateState {
            copy(
                executionResult = BaseUiState.Idle,
                userInput = ""
            )
        }
    }
}

private fun getDefaultCode(language: ProgrammingLanguage): String {
    return when (language) {
        ProgrammingLanguage.PYTHON -> """# Python Calculator Example
def calculator():
    a = float(input("Enter first number: "))
    operator = input("Enter operator (+, -, *, /): ")
    b = float(input("Enter second number: "))
    
    if operator == '+':
        result = a + b
    elif operator == '-':
        result = a - b
    elif operator == '*':
        result = a * b
    elif operator == '/':
        result = a / b if b != 0 else "Cannot divide by zero"
    else:
        result = "Invalid operator"
    
    print(f"Result: {result}")

calculator()"""

        ProgrammingLanguage.JAVA -> """// Java Hello World Example
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World from Java!");
        
        // Simple calculator
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = scanner.nextInt();
        System.out.println("You entered: " + num);
    }
}"""

        ProgrammingLanguage.KOTLIN -> """// Kotlin Example
fun main() {
    println("Hello, World from Kotlin!")
    
    print("Enter your name: ")
    val name = readLine() ?: "Unknown"
    println("Hello, ${'$'}name!")
}"""

        ProgrammingLanguage.JAVASCRIPT -> """// JavaScript Example
function calculator() {
    console.log("JavaScript Calculator");
    
    // In a real environment, you'd use prompt() or input
    const a = 10;
    const b = 5;
    
    console.log(a + " + " + b + " = " + (a + b));
    console.log(a + " - " + b + " = " + (a - b));
    console.log(a + " * " + b + " = " + (a * b));
    console.log(a + " / " + b + " = " + (a / b));
}

calculator();"""

        ProgrammingLanguage.CPP -> """// C++ Example
#include <iostream>
using namespace std;

int main() {
    cout << "Hello, World from C++!" << endl;
    
    int num;
    cout << "Enter a number: ";
    cin >> num;
    cout << "You entered: " << num << endl;
    
    return 0;
}"""

        ProgrammingLanguage.C -> """// C Example
#include <stdio.h>

int main() {
    printf("Hello, World from C!\\n");
    
    int num;
    printf("Enter a number: ");
    scanf("%d", &num);
    printf("You entered: %d\\n", num);
    
    return 0;
}"""
    }
}
