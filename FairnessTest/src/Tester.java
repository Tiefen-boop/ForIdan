import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Tester {
    private static final int NUMBER_OF_ASSIGNMENTS = 10000;
    private static final int MINIMUM_GROUPS = 7;
    private static final int MAXIMUM_GROUPS = 19;
    private static final int MINIMUM_MEMBERS = 800;
    private static final int MAXIMUM_MEMBERS = 1000;
    private static final int MINIMUM_MEMBERS_PER_GROUP = 2;


    private final Map<Integer, Group> groupMap = new HashMap<>();

    private int numberOfGroups;
    private int totalNumberOfMembers;


    public void init(){
        Random random = new Random();
        int groupID, numOfGroupsLeftToAssign;
        Group group;

        int leftToAssign = totalNumberOfMembers = MINIMUM_MEMBERS + random.nextInt(1 + (MAXIMUM_MEMBERS - MINIMUM_MEMBERS));
        numberOfGroups = MINIMUM_GROUPS + random.nextInt(1 + (MAXIMUM_GROUPS - MINIMUM_GROUPS));


        // Assigning members to groups
        for (numOfGroupsLeftToAssign = numberOfGroups; numOfGroupsLeftToAssign > 1; numOfGroupsLeftToAssign--){
            groupID = numOfGroupsLeftToAssign;
            group = new Group(groupID);
            int groupSize;

            // every group has at least 2 members, making sure leaving enough left for the rest
            leftToAssign -= (groupSize = MINIMUM_MEMBERS_PER_GROUP + random.nextInt(1 + leftToAssign - (MINIMUM_MEMBERS_PER_GROUP * (numOfGroupsLeftToAssign + 1))));
            if (leftToAssign < (numOfGroupsLeftToAssign * MINIMUM_MEMBERS_PER_GROUP))
                throw new RuntimeException(
                        String.format("Shitty calculation, not enough left for the rest of the groups --- leftGroups=%d, leftToAssing=%d", numOfGroupsLeftToAssign, leftToAssign)
                );

            // assigning new members for the group
            for (int j = 0; j < groupSize; j++)
                group.addMember(new Member(groupID));
            groupMap.put(groupID, group);
        }

        // assigning the rest for the last group
        groupID = numOfGroupsLeftToAssign;
        group = new Group(groupID);
        // assigning new members for the group
        for (int j = 0; j < leftToAssign; j++)
            group.addMember(new Member(groupID));
        groupMap.put(groupID, group);
    }

    public void run(){
        for (int  i = 0 ; i < NUMBER_OF_ASSIGNMENTS ; i++){
            Member assignee =
                    nextAssignee(
                            groupMap.values().stream()
                                    .map(Group::getNextAssignee)
                                    .collect(Collectors.toSet())
                    );
            Member backup = groupMap.get(assignee.getGroupID()).getBackupFor(assignee);
            assignee.incAssingments();
            backup.incAssingments();
            if (assignee.didAssignment()) {
                assignee.incDoneAssignments();
            } else {
                backup.incDoneAssignments();
            }
        }
        System.out.printf("Results for [NumberOfAssignments: %d, NumOfGroups: %d, NumOfMembers: %d]:%n", NUMBER_OF_ASSIGNMENTS, numberOfGroups, totalNumberOfMembers);
        for (Group group: groupMap.values())
            System.out.println(group.finalResult());
    }

    /**
     * @param members Non-Empty group of members
     * @return member with least done assignments
     */
    public static Member nextAssignee(Set<Member> members){
        return members.stream()
                .min((m1, m2) -> m1.getDoneAssignmentCount() - m2.getDoneAssignmentCount())
                .get();
    }
}
