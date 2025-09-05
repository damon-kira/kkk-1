package com.kira.learning.base.validation

/**
 * 验证结果
 */
sealed interface ValidationResult {
    object Valid : ValidationResult
    data class Invalid(val message: String) : ValidationResult
}

/**
 * 表单字段验证器
 */
interface FieldValidator {
    fun validate(value: String): ValidationResult
}

/**
 * 常用验证器
 */
object Validators {

    fun required(message: String = "此字段为必填项"): FieldValidator = object : FieldValidator {
        override fun validate(value: String): ValidationResult {
            return if (value.isBlank()) {
                ValidationResult.Invalid(message)
            } else {
                ValidationResult.Valid
            }
        }
    }

    fun email(message: String = "请输入有效的邮箱地址"): FieldValidator = object : FieldValidator {
        override fun validate(value: String): ValidationResult {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
            return if (value.matches(emailRegex)) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(message)
            }
        }
    }

    fun minLength(length: Int, message: String = "最少需要${length}个字符"): FieldValidator = object : FieldValidator {
        override fun validate(value: String): ValidationResult {
            return if (value.length >= length) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(message)
            }
        }
    }

    fun maxLength(length: Int, message: String = "最多${length}个字符"): FieldValidator = object : FieldValidator {
        override fun validate(value: String): ValidationResult {
            return if (value.length <= length) {
                ValidationResult.Valid
            } else {
                ValidationResult.Invalid(message)
            }
        }
    }

    fun combine(vararg validators: FieldValidator): FieldValidator = object : FieldValidator {
        override fun validate(value: String): ValidationResult {
            validators.forEach { validator ->
                val result = validator.validate(value)
                if (result is ValidationResult.Invalid) {
                    return result
                }
            }
            return ValidationResult.Valid
        }
    }
}

/**
 * 表单状态管理
 */
data class FormField(
    val value: String = "",
    val validators: List<FieldValidator> = emptyList(),
    val error: String? = null
) {
    fun validate(): FormField {
        validators.forEach { validator ->
            when (val result = validator.validate(value)) {
                is ValidationResult.Invalid -> return copy(error = result.message)
                ValidationResult.Valid -> {}
            }
        }
        return copy(error = null)
    }

    val isValid: Boolean get() = error == null
}

/**
 * 表单状态
 */
data class FormState(
    val fields: Map<String, FormField> = emptyMap()
) {
    fun updateField(key: String, value: String): FormState {
        val field = fields[key] ?: FormField()
        return copy(
            fields = fields + (key to field.copy(value = value).validate())
        )
    }

    fun validateAll(): FormState {
        return copy(
            fields = fields.mapValues { it.value.validate() }
        )
    }

    val isValid: Boolean get() = fields.values.all { it.isValid }
}
