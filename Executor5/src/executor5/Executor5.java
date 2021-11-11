/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executor5;

/**
 *
 * @author ewelina
 */
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Executor5 {

    private static final Instant INICIO = Instant.now();
    private static int contadorTareas = 1;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        List<Callable<String>> tareas = Stream.generate(Executor5::getTareaSleepUnSegundo)
                        .limit(5)
                        .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Future<String>> futures = executor.invokeAll(tareas);
        for (Future<String> future : futures) {
            String resultado = future.get();
            Log(resultado);
        }

        Log("El hilo principal continúa...");

        String resultadoAny = executor.invokeAny(tareas);
        Log(resultadoAny);

        Log("El hilo principal continúa...");
        executor.shutdown();
    }

    private static Callable<String> getTareaSleepUnSegundo() {
        int numeroTarea = contadorTareas++;

        return () -> {
            Log("Inicio de la tarea " + numeroTarea);
            try {
                TimeUnit.SECONDS.sleep(1);
                Log("Finaliza la tarea " + numeroTarea);
                return "resultado de la tarea " + numeroTarea;
            } catch (InterruptedException e) {
                Log("sleep ha sido interrumpido en tarea " + numeroTarea);
                return null;
            }
        };
    }

    private static void Log(Object mensaje) {
        System.out.println(String.format("%s [%s] %s",
                Duration.between(INICIO, Instant.now()), Thread.currentThread().getName(), mensaje.toString()));
    }
}
