package roomescape.annotations;

import jakarta.validation.GroupSequence;
import roomescape.annotations.ValidationGroups.NotBlankGroup;
import roomescape.annotations.ValidationGroups.PatternGroup;

@GroupSequence({NotBlankGroup.class, PatternGroup.class})
public interface ValidationSequence {
}
