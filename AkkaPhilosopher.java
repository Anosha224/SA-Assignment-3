package DinningPhilosophers;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class AkkaPhilosopher extends UntypedActor {
	public static Props mkProps(String aName, ActorRef aWeiter) {
        return Props.create(AkkaPhilosopher.class, aName, aWeiter);
    }

    private String name;
    private ActorRef weiter;
    private static final int THINK_TIME = 3000;
    private static final int EAT_TIME = 3000;

    private AkkaPhilosopher(String aName, ActorRef ,aWeiter) {
        name = aName;
        weiter = aWeiter;
        aWeiter.tell(new Messages.Introduce(aName), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AkkaMessages.Think) {
            System.out.println(name + " thinking");
            Thread.sleep(THINK_TIME);
            System.out.println(name + " gets hungry");
            weiter.tell(new AkkaMessages.Hungry(), getSelf());

        } else if (message instanceof AkkaMessages.Eat) {
            System.out.println(name + " eating");
            Thread.sleep(EAT_TIME);
            System.out.println(name + " fed up");
            weiter.tell(new AkkaMessages.FinishEat(), getSelf());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ActorSystem system = ActorSystem.create();
        final int FORKS = 5;
        ActorRef waiter = system.actorOf(AkkaWaiter.mkProps(FORKS));

        ActorRef Socrates   = system.actorOf(AkkaPhilosopher.mkProps("Socrates", waiter));
        ActorRef Aristotle  = system.actorOf(AkkaPhilosopher.mkProps("Aristotle", waiter));
        ActorRef Pifagor    = system.actorOf(AkkaPhilosopher.mkProps("Pifagor", waiter));
        ActorRef Platon     = system.actorOf(AkkaPhilosopher.mkProps("Platon", waiter));
        ActorRef Ptolemy    = system.actorOf(AkkaPhilosopher.mkProps("Ptolemy", waiter));
    }

}
