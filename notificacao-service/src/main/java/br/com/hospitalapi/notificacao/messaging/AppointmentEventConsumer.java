package br.com.hospitalapi.notificacao.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventConsumer {

    @RabbitListener(queues = "appointment.notification.queue")
    public void consume(AppointmentEvent event) {

        System.out.println("=================================");
        System.out.println("NOTIFICATION RECEIVED");
        System.out.println("Event: " + event.eventType());
        System.out.println("Appointment: " + event.appointmentId());
        System.out.println("Patient: " + event.patientId());
        System.out.println("Doctor: " + event.doctorId());
        System.out.println("Date: " + event.appointmentDateTime());
        System.out.println("=================================");
    }
}
