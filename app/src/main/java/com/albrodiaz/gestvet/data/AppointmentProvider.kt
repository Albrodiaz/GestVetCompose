package com.albrodiaz.gestvet.data

import com.albrodiaz.gestvet.ui.views.models.AppointmentModel

class AppointmentProvider {

    companion object {
        val appointments: List<AppointmentModel> = mutableListOf(
            AppointmentModel(
                "Alberto Rodríguez Díaz",
                "Marley",
                "12/12/2022",
                "12:00",
                "Baño",
                "Detalle de la cita",
                1
            ),
            AppointmentModel(
                "Paloma Genescá Gómez",
                "Peaky",
                "12/12/2022",
                "12:00",
                "Vacuna",
                "Detalle de la cita",
                2
            ),
            AppointmentModel(
                "Jeanine Gómez Álvarez",
                "Rumbo",
                "12/12/2022",
                "12:00",
                "Revisión",
                "Detalle de la cita",
                3
            ),
            AppointmentModel(
                "Propietario 4",
                "Mascota 5",
                "12/12/2022",
                "12:00",
                "Revisión",
                "Detalle de la cita",
                4
            ),
            AppointmentModel(
                "Propietario 6",
                "Mascota 6",
                "12/12/2022",
                "12:00",
                "Asunto",
                "Detalle de la cita",
                5
            ),
            AppointmentModel(
                "Propietario 7",
                "Mascota 7",
                "12/12/2022",
                "12:00",
                "Asunto",
                "Detalle de la cita",
                6
            ),
            AppointmentModel(
                "Propietario 8",
                "Mascota 8",
                "12/12/2022",
                "12:00",
                "Asunto",
                "Detalle de la cita",
                7
            )
        )
    }

}