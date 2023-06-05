import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {
    private final int groupID;
    private final Set<Member> members = new HashSet<>();

    public Group(int groupID) {
        this.groupID = groupID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void addMember(Member toAdd){
        members.add(toAdd);
    }

    public int getGroupSize(){
        return members.size();
    }

    public Member getNextAssignee(){
        return Tester.nextAssignee(members);
    }

    public Member getBackupFor(Member member){
        return
                Tester.nextAssignee(
                        members.stream().filter(m -> m != member).collect(Collectors.toSet()) // excluding given member
                );
    }

    public double avgOfAssignmentsPerMember(){
        return members.stream()
                .mapToDouble(Member::getAssignmentsCount)
                .average()
                .getAsDouble();
    }

    public String finalResult(){
        return
                String.format(
                        "Group: '%d', Size: %d, AvgAssignment: %2f, AvgToSize-Ratio: %2f",
                        groupID,
                        getGroupSize(),
                        avgOfAssignmentsPerMember(),
                        avgOfAssignmentsPerMember()/getGroupSize()
                );
    }
}
