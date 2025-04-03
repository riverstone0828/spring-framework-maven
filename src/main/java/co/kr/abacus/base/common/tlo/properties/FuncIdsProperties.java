package co.kr.abacus.base.common.tlo.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FuncIdsProperties {
    private List<FuncIdVO> permitAllFuncIds;
}
