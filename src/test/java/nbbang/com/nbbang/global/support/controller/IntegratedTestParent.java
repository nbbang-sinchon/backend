package nbbang.com.nbbang.global.support.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@MockBean(JpaMetamodelMappingContext.class)
@Import({ControllerTestUtil.class})
public abstract class IntegratedTestParent {
}
