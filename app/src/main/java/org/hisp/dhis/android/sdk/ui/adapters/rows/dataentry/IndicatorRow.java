/*
 *  Copyright (c) 2015, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.ui.adapters.rows.dataentry;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis.android.sdk.persistence.models.ProgramIndicator;

public final class IndicatorRow implements DataEntryRow {
    private static final String EMPTY_FIELD = "";

    private final ProgramIndicator mIndicator;
    private String mValue;

    private boolean hidden = false;
    private boolean editable = true;

    public IndicatorRow(ProgramIndicator indicator, String value) {
        mIndicator = indicator;
        mValue = value;
    }

    @Override
    public View getView(FragmentManager fragmentManager, LayoutInflater inflater,
                        View convertView, ViewGroup container) {
        View view;
        IndicatorViewHolder holder;

        if (convertView != null && convertView.getTag() instanceof IndicatorViewHolder) {
            view = convertView;
            holder = (IndicatorViewHolder) view.getTag();
        } else {
            View root = inflater.inflate(
                    R.layout.listview_row_indicator, container, false);
            holder = new IndicatorViewHolder(
                    (TextView) root.findViewById(R.id.text_label),
                    (TextView) root.findViewById(R.id.indicator_row)
            );

            root.setTag(holder);
            view = root;
        }

        if (mIndicator.getName()!= null) {
            holder.textLabel.setText(mIndicator.getName());
        } else {
            holder.textLabel.setText(EMPTY_FIELD);
        }

        if(!isEditable())
        {
            holder.textValue.setEnabled(false);
        }
        else
            holder.textValue.setEnabled(true);

        holder.textValue.setText(mValue);
        return view;
    }

    @Override
    public int getViewType() {
        return DataEntryRowTypes.INDICATOR.ordinal();
    }

    @Override
    public BaseValue getBaseValue() {
        return null;
    }

    public void updateValue(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    public ProgramIndicator getIndicator() {
        return mIndicator;
    }

    public static class IndicatorViewHolder {
        final TextView textLabel;
        final TextView textValue;

        public IndicatorViewHolder(TextView textLabel,
                                   TextView textValue) {
            this.textLabel = textLabel;
            this.textValue = textValue;
        }
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
