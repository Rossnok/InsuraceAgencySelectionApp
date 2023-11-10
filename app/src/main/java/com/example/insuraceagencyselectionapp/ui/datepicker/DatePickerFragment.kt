package com.example.insuraceagencyselectionapp.ui.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(val listener: (day:Int, month:Int, year:Int) -> Unit): DialogFragment(),
    DatePickerDialog.OnDateSetListener {// se crea un franment que muestra un datePicker dialog para seleccionar la fecha de la cita

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()//instanciado del calendario
        val day = calendar.get(Calendar.DAY_OF_MONTH)// obtener dia
        val month = calendar.get(Calendar.MONTH)// obtener mes
        val year = calendar.get(Calendar.YEAR)// obtener año

        val picker = DatePickerDialog(activity as Context, this, year, month, day)// crear el picker

        return picker // retornar el picker
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {// fecha seleccionada
        listener(dayOfMonth, month, year) // dia, mes y año seleccionado
    }

}