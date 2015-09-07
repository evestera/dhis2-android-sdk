package org.hisp.dhis.android.sdk.core.persistence.models.metadata;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.hisp.dhis.android.sdk.core.persistence.models.flow.DataElement$Flow;
import org.hisp.dhis.android.sdk.core.persistence.models.flow.DataElement$Flow$Table;
import org.hisp.dhis.android.sdk.models.common.IIdentifiableObjectStore;
import org.hisp.dhis.android.sdk.models.metadata.DataElement;

import java.util.List;

public final class DataElementStore implements IIdentifiableObjectStore<DataElement> {

    public DataElementStore() {
        //empty constructor
    }

    @Override
    public void insert(DataElement object) {
        DataElement$Flow dataElementFlow = DataElement$Flow.fromModel(object);
        dataElementFlow.insert();

        object.setId(dataElementFlow.getId());
    }

    @Override
    public void update(DataElement object) {
        DataElement$Flow.fromModel(object).update();
    }

    @Override
    public void save(DataElement object) {
        DataElement$Flow dataElementFlow =
                DataElement$Flow.fromModel(object);
        dataElementFlow.save();

        object.setId(dataElementFlow.getId());
    }

    @Override
    public void delete(DataElement object) {
        DataElement$Flow.fromModel(object).delete();
    }

    @Override
    public List<DataElement> query() {
        List<DataElement$Flow> dataElementFlows = new Select()
                .from(DataElement$Flow.class)
                .queryList();
        return DataElement$Flow.toModels(dataElementFlows);
    }

    @Override
    public DataElement query(long id) {
        DataElement$Flow dataElementFlow = new Select()
                .from(DataElement$Flow.class)
                .where(Condition.column(DataElement$Flow$Table.ID).is(id))
                .querySingle();
        return DataElement$Flow.toModel(dataElementFlow);
    }

    @Override
    public DataElement query(String uid) {
        DataElement$Flow dataElementFlow = new Select()
                .from(DataElement$Flow.class)
                .where(Condition.column(DataElement$Flow$Table.UID).is(uid))
                .querySingle();
        return DataElement$Flow.toModel(dataElementFlow);
    }
}
