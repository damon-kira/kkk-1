package com.kira.learning.modules.coding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.UiEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CodingViewState(
    val code: String = getDefaultCode(ProgrammingLanguage.PYTHON),
    val selectedLanguage: ProgrammingLanguage = ProgrammingLanguage.PYTHON,
    val executionResult: BaseUiState<CodeExecutionResponse> = BaseUiState.Idle,
    val userInput: String = "",
    val showLineNumbers: Boolean = true,
    val isExecuting: Boolean = false
) : ViewState()

sealed interface CodingEvent {
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
) : BaseViewModel<CodingViewState, UiEvent>(
    initialState = CodingViewState()
) {

    override fun handleAction(action: Any) {
        when (action) {
            is CodingEvent.UpdateCode -> updateCode(action.code)
            is CodingEvent.ChangeLanguage -> changeLanguage(action.language)
            is CodingEvent.UpdateUserInput -> updateUserInput(action.input)
            is CodingEvent.ExecuteCode -> executeCode()
            is CodingEvent.ToggleLineNumbers -> toggleLineNumbers()
            is CodingEvent.ClearResult -> clearResult()
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

        viewModelScope.launch {
            updateState {
                copy(
                    isExecuting = true,
                    executionResult = BaseUiState.Loading
                )
            }

            val request = CodeExecutionRequest(
                code = currentState.code,
                language = currentState.selectedLanguage.displayName,
                input = currentState.userInput
            )

            when (val result = repository.executeCode(request)) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            isExecuting = false,
                            executionResult = BaseUiState.Success(result.data)
                        )
                    }
                }
                is ApiResult.Error -> {
                    updateState {
                        copy(
                            isExecuting = false,
                            executionResult = BaseUiState.Error(result.message)
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }
                else -> {
                    updateState {
                        copy(
                            isExecuting = false,
                            executionResult = BaseUiState.Error("Unknown error occurred")
                        )
                    }
                }
            }
        }
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
import kotlin.util.Scanner;

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
