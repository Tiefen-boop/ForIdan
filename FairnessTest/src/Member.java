import java.util.Objects;
import java.util.Random;

public class Member {
    private static Random RANDOM = new Random();

    private final int groupID;
    private int assignmentsCount = 0;
    private int doneAssignmentCount = 0;

    public Member(int groupID) {
        this.groupID = groupID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getAssignmentsCount() {
        return assignmentsCount;
    }

    public int getDoneAssignmentCount() {
        return doneAssignmentCount;
    }

    public void incAssingments(){
        assignmentsCount++;
    }

    public void incDoneAssignments(){
        doneAssignmentCount++;
    }

    public boolean didAssignment(){
        return RANDOM.nextDouble() < 0.85; // every member has 15% chance of not doing the assignment
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupID, assignmentsCount, doneAssignmentCount);
    }
}
