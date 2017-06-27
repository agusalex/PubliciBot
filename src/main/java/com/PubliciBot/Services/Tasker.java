package com.PubliciBot.Services;

import com.PubliciBot.DAO.Interfaces.Task;
import com.PubliciBot.DAO.Neodatis.DAONeodatis;
import com.PubliciBot.DM.Campana;
import com.PubliciBot.DM.EstadoCampana;
import com.PubliciBot.DM.Post;

import java.time.Instant;
import java.util.*;

public class Tasker extends Thread{

//SINGLETON
    private static Tasker tasker;
    private static Stack<Task> tasks;
    private static boolean run=false;
    private static ArrayList<Sender> senders;
    private static HashSet<Post> dbtasks;


    private Tasker (){
        tasks=new Stack<>();
        senders =new ArrayList<>();
        dbtasks=new HashSet<>();
        int cantsenders=1;
        for(int i=0; i<1;i++) {
            Sender sender = new Sender(this);
            senders.add(sender);
        }
        DAONeodatis daoNeodatis= new DAONeodatis();

        ArrayList<Campana> campanas=(ArrayList<Campana>)daoNeodatis.obtenerTodos(Campana.class);

        Instant NOW=Instant.now();
        long nowinSeconds=NOW.getEpochSecond();



        for( Campana campana : campanas) {
            if (campana.getFechaInicio().before(Date.from(NOW))) {
                    campana.setEstadoCampana(EstadoCampana.ACTIVA);
                    for(Post post :campana.getPosts()) {
                        dbtasks.add(post);
                    }
            }

        if(campana.calcularCaducidad().before(Date.from(NOW))){
            campana.setEstadoCampana(EstadoCampana.FINALIZADA);
        }
    }


}

    @Override
    public void run() {

        boolean run=true;

        while (run) {
            System.out.println("\n\n");
            System.out.println("/////////Tasker: Buscando Tareas...//////////");
            buscaTareas();
            for(Sender sender : senders){
                sender.run();
            }
            System.out.println("////////////Tasker: sleep por 10 segundos///////////////");
            System.out.println("\n\n");

            try {
                Thread.sleep(10000); //se duerme y vuelve a mandar cada 1 minuto
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }




    public void buscaTareas(){
        DAONeodatis daoNeodatis= new DAONeodatis();
        Instant NOW=Instant.now();
        long nowinSeconds=NOW.getEpochSecond();

        System.out.println(dbtasks);

        for (Post p:dbtasks
             ) {

           if(p!=null) {
               System.out.println("\n\n");
               System.out.println("Tarea encontrada: " + p.getAccion().getNombreAccion());

               System.out.print("Fecha inicio: " + p.getFechaInicio() + " es antes que ahora?: " + Date.from(NOW));
               if (p.getFechaInicio().before(Date.from(NOW))) { //Si el Post Todavia Esta vigente (supero la fecha de inicio)
                   System.out.println("  SI");

                   System.out.print("FechaCaducidad: " + p.getFechaCaducidad() + " es despues que ahora?: " + Date.from(NOW));
                   if (p.getFechaCaducidad().after((Date.from(NOW)))) {  //Si no supero la fecha de caducidad
                       System.out.println(" SI");


                       if (p.getFechaUltimaEjecucion() != null) {
                           long ultimaejSeconds = p.getFechaUltimaEjecucion().toInstant().getEpochSecond();   //Convertir ultima ejecucion a segundos

                           System.out.print("Cooldown: Supero el Cooldown? ");
                           if (Math.abs(nowinSeconds - ultimaejSeconds) > p.getAccion().getPeriodicidadSegundos()) {  //SI LE TOCA EJECUTARSE (PASO EL COOLDOWN )
                               System.out.println("Cooldown: SI, agregada a col de ejecucion");
                               tasks.add(p);
                               return;
                           }
                       } else {
                           System.out.println("Cooldown: Nunca ejecutada antes, agegada a cola de ejecucion");
                           System.out.println("\n");
                           tasks.add(p);
                           return;
                       }


                   }


               }

               System.out.println(" NO: NO CALIFICA PARA EJECUCION: " + p.getAccion().getNombreAccion());
               System.out.println("\n\n");
           }
        }



    }



    public Task giveMeaTask(){
        if(tasks.size()!=0) {
            return tasks.pop();
        }
        return null;
    }

    public void stopTasker(){
        run=false;
    }

    public static void addTask(Task task){
       dbtasks.add((Post)task);
    }

    public static void removeTask(Task task){
        dbtasks.remove(task);

    }



    public static Tasker getTasker(){
        if(tasker==null){
            tasker=new Tasker();
        }
        return tasker;
    }



}
