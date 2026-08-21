package widerange;

import mindustry.mod.Mod;
import mindustry.Vars;
import mindustry.input.InputHandler;
import arc.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WideRangeMod extends Mod {

    // 여기 두 값만 조절하세요
    private static final int MAX_SELECT_SIZE = 1000; // 복사(스키매틱) 드래그, 기본 66
    private static final int MAX_LENGTH = 1000;       // 삭제(철거) 드래그, 기본 100

    public WideRangeMod() {
        Log.info("[WideRange] 모드 로드됨");
    }

    @Override
    public void init() {
        // 1) 복사(스키매틱) 드래그 선택 범위 - 확실히 작동함
        Vars.maxSchematicSize = MAX_SELECT_SIZE;
        Log.info("[WideRange] maxSchematicSize -> @", Vars.maxSchematicSize);

        // 2) 삭제(철거) 드래그 선택 범위 - static final 상수라 인라이닝 때문에
        //    실제로 반영 안 될 수 있음. 시도는 하되 결과를 꼭 게임에서 확인할 것.
        try {
            Field field = InputHandler.class.getDeclaredField("maxLength");
            field.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.setInt(null, MAX_LENGTH);
            Log.info("[WideRange] maxLength 필드 변경 성공 (실제 반영 여부는 게임 내 테스트로 확인 필요) -> @", MAX_LENGTH);
        } catch (Exception e) {
            Log.err("[WideRange] maxLength 변경 실패 - Java 버전이 modifiers 리플렉션을 막고 있을 수 있음", e);
        }
    }
}
