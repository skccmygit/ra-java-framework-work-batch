package kr.co.skcc.base.bap.scheduler.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobParamType {
    STRING,
    DATE,
    LONG,
    DOUBLE;
}