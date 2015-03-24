package adept.common;

import com.google.common.collect.ImmutableSet;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the script event classes
 */

public class ScriptTest {

    // Demonstrates using the script classes by representing OSU's example from
    // their script generation algorithm document.
    @Test
    public void scriptTest() {
        final Type personType = new Type("Person");
        final Type crimeType = new Type("Crime");
        final Type timeType = new Type("Time");
        final Type sentenceType = new Type("Sentence");
        final Type durationType = new Type("Duration");
        final Type courtType = new Type("Court");

        final Type JusticeChargeIndict = new Type("Justice.Charge-Indict");
        final Type JusticeTrial = new Type("Justice.Trial");
        final Type JusticeSentence = new Type("Justice.Trial");
        final Type JusticeConvict = new Type("Justice.Convict");
        final Type JusticeAppeal = new Type("Justice.Convict");

        final Type defendantRole = new Type("Defendant");
        final Type crimeRole = new Type("Crime");
        final Type timeRole = new Type("Time");
        final Type sentenceRole = new Type("Role");
        final Type prosecutorRole = new Type("Prosecutor");
        final Type adjudicatorRole = new Type("Prosecutor");

        final ScriptVariable theCrook = ScriptVariable.createOfType(personType);
        final ScriptVariable theCrime = ScriptVariable.createOfType(crimeType);
        final ScriptVariable indictmentTimeVariable = ScriptVariable.createOfType(timeType);

        final ScriptEvent indictmentEvent = ScriptEvent.create(JusticeChargeIndict,
                ImmutableSet.of(
                        ScriptEventArgument.create(defendantRole, theCrook),
                        ScriptEventArgument.create(crimeRole, theCrime),
                        ScriptEventArgument.create(timeRole, indictmentTimeVariable)));

        final ScriptVariable trialTimeVariable = ScriptVariable.createOfType(timeType);
        final ScriptEvent trial = ScriptEvent.create(JusticeTrial, ImmutableSet.of(
                ScriptEventArgument.create(defendantRole, theCrook),
                ScriptEventArgument.create(timeRole, trialTimeVariable)));

        final ScriptVariable sentenceVariable = ScriptVariable.createOfTypes(
                ImmutableSet.of(sentenceType, durationType));
        final ScriptEvent sentenceEvent = ScriptEvent.create(JusticeSentence, ImmutableSet.of(
                ScriptEventArgument.create(defendantRole, theCrook),
                ScriptEventArgument.create(sentenceRole, sentenceVariable)));

        final ScriptVariable theProsecutor = ScriptVariable.createOfType(personType);
        final ScriptVariable convictionTimeVariable = ScriptVariable.createOfType(timeType);
        final ScriptEvent convictEvent = ScriptEvent.create(JusticeConvict, ImmutableSet.of(
                ScriptEventArgument.create(defendantRole, theCrook),
                ScriptEventArgument.create(prosecutorRole, theProsecutor),
                ScriptEventArgument.create(crimeRole, theCrime),
                ScriptEventArgument.create(timeRole, convictionTimeVariable)));

        final ScriptVariable theCourt = ScriptVariable.createOfType(courtType);
        final ScriptEvent appealEvent = ScriptEvent.create(JusticeAppeal, ImmutableSet.of(
                ScriptEventArgument.create(defendantRole, theCrook),
                ScriptEventArgument.create(adjudicatorRole, theCourt)));

        final Type causalInfluence = new Type("causalInfluence");
        final ImmutableSet<ScriptLink> links = ImmutableSet.of(
                ScriptLink.create(causalInfluence, indictmentEvent, trial, 0.9f),
                ScriptLink.create(causalInfluence, trial, sentenceEvent, 0.6f),
                ScriptLink.create(causalInfluence, sentenceEvent, convictEvent, 0.7f),
                ScriptLink.create(causalInfluence, convictEvent, appealEvent, 0.4f));

        final Type isLaterThan = new Type("isLaterThan");
        final ImmutableSet<ScriptVariableBinaryConstraint> constraints = ImmutableSet.of(
                ScriptVariableBinaryConstraint.create(isLaterThan, trialTimeVariable, indictmentTimeVariable),
                ScriptVariableBinaryConstraint.create(isLaterThan, convictionTimeVariable, trialTimeVariable));

        final Script justiceScript = Script.create(
                ImmutableSet.of(trial, sentenceEvent, convictEvent, appealEvent),
                links, constraints);

        System.out.println("Script test passed.");
    }
}
