package uk.co.onsdigital.discovery.controller;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import uk.co.onsdigital.discovery.dao.DatasetDAO;
import uk.co.onsdigital.discovery.model.DatasetMetadata;
import uk.co.onsdigital.discovery.validation.MetadataValidator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.onsdigital.discovery.controller.MetadataController.DATASETS_LIST_KEY;

/**
 * Created by dave on 24/01/2017.
 */
public class MetadataControllerAPITest {

    private MetadataController api;

    @Mock
    private Model mockModel;

    @Mock
    private MetadataValidator mockValidator;

    @Mock
    private BindingResult mockBindingResult;

    @Mock
    private DatasetDAO mockDatasetDAO;

    private List<String> datasetIds;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        datasetIds = new ArrayList<>();
        datasetIds.add("DS1");
        datasetIds.add("DS2");
        datasetIds.add("DS3");

        api = new MetadataController();
        ReflectionTestUtils.setField(api, "validator", mockValidator);
        ReflectionTestUtils.setField(api, "datasetDAO", mockDatasetDAO);
    }

    @Test
    public void shouldGetMetadataForm() {
        String result = api.getMetadataForm(mockModel);
        assertThat(result, equalTo(MetadataController.EDITOR_VIEW));
        verify(mockModel, times(1)).addAttribute(eq(MetadataController.MODEL_KEY), any(DatasetMetadata.class));
    }

    @Test
    public void shouldReturnEditorViewForValidationErrors() throws Exception {
        DatasetMetadata form = new DatasetMetadata();
        form.setJsonMetadata("Patter cake, patter cake, patter cake man, 'Data Bake' me a cake as fast as you can!");

        doAnswer((invocationOnMock -> null))
                .when(mockValidator).validate(form, mockBindingResult);
        when(mockBindingResult.hasErrors())
                .thenReturn(true);
        when(mockDatasetDAO.getDatasetIds())
                .thenReturn(datasetIds);

        String result = api.metadataSubmit(form, mockModel, mockBindingResult);
        verify(mockValidator, times(1)).validate(form, mockBindingResult);
        verify(mockDatasetDAO, times(1)).getDatasetIds();
        verify(mockDatasetDAO, never()).createOrUpdateMetadata(any(DatasetMetadata.class));
        verify(mockModel, times(1)).addAttribute(DATASETS_LIST_KEY, datasetIds);
        assertThat(result, equalTo(MetadataController.EDITOR_VIEW));
    }

}
