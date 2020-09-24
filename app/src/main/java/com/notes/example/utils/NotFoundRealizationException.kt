package com.notes.example.utils

class NotFoundRealizationException(defValue: Any?) : Exception("not found realization for $defValue")