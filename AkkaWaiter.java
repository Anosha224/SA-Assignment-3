package DinningPhilosophers;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Arrays;
public class AkkaWaiter extends UntypedActor {
	public static Props mkProps(int forkCount) {
        return Props.create(AkkaWaiter.class, forkCount);
    }

    private enum ForkState { FREE, USED }
    private ForkState[] mForks;
    private ArrayList<ActorRef> mPhilosophers;

    private AkkaWaiter(int forkCount) {
        mForks = new ForkState[forkCount];
        Arrays.fill(mForks, ForkState.FREE);
        mPhilosophers = new ArrayList<ActorRef>(forkCount);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AkkaMessages.Introduce) {
            String name = ((AkkaMessages.Introduce) message).getPhilosopherName();
            System.out.println(name + " joined table. Welcome!");
            mPhilosophers.add(getSender());
            getSender().tell(new AkkaMessages.Think(), getSelf());

        }  else if (message instanceof AkkaMessages.Hungry) {
            int seat = mPhilosophers.indexOf(getSender());
            if (seat == -1) {
                System.out.println("I don`t know this philosopher");
            } else {
                int leftFork = seat;
                int rightFork = (seat + 1) % mForks.length;
                if (mForks[leftFork].equals(ForkState.FREE) && mForks[rightFork].equals(ForkState.FREE)) {
                    mForks[leftFork] = ForkState.USED;
                    mForks[rightFork] = ForkState.USED;
                    getSender().tell(new AkkaMessages.Eat(), getSelf());
                } else {
                    getSender().tell(new AkkaMessages.Think(), getSelf());
                }
            }
        } else if (message instanceof AkkaMessages.FinishEat) {
            int seat = mPhilosophers.indexOf(getSender());
            int leftFork = seat;
            int rightFork = (seat + 1) % mForks.length;
            mForks[leftFork] = ForkState.FREE;
            mForks[rightFork] = ForkState.FREE;
            getSender().tell(new AkkaMessages.Think(), getSelf());
        }

    }

}
