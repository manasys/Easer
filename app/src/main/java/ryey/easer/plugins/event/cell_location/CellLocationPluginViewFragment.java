/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.plugins.event.cell_location;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import ryey.easer.R;
import ryey.easer.Utils;
import ryey.easer.commons.local_plugin.InvalidDataInputException;
import ryey.easer.commons.local_plugin.ValidData;
import ryey.easer.plugins.PluginViewFragment;
import ryey.easer.plugins.reusable.CellLocationSingleData;
import ryey.easer.plugins.reusable.ScannerDialogFragment;

public class CellLocationPluginViewFragment extends PluginViewFragment<CellLocationEventData> implements ScannerDialogFragment.ScannerListener  {

    public static final int DIALOG_FRAGMENT = 1;

    private EditText editText;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.plugin_event__cell_location, container, false);

        editText = view.findViewById(R.id.location_text);
        view.findViewById(R.id.location_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION))
                    return;

                DialogFragment dialogFrag = new ScannerDialogFragment();
                dialogFrag.setTargetFragment(CellLocationPluginViewFragment.this, DIALOG_FRAGMENT);
                dialogFrag.show(getFragmentManager(), "dialog");

            }
        });

        return view;
    }

    @Override
    protected void _fill(@ValidData @NonNull CellLocationEventData data) {
        editText.setText(data.toString());
    }

    @ValidData
    @NonNull
    @Override
    public CellLocationEventData getData() throws InvalidDataInputException {
        CellLocationEventData data = CellLocationEventData.fromString(editText.getText().toString());
        return data;
    }

    @Override
    public void onPositiveClicked(List<CellLocationSingleData> singleDataList) {
        String display_str = editText.getText().toString();
        StringBuilder stringBuilder = new StringBuilder(display_str);
        for (CellLocationSingleData singleData : singleDataList) {
            stringBuilder.append('\n')
                    .append(singleData.toString());
        }
        if (stringBuilder.charAt(0) == '\n')
            stringBuilder.deleteCharAt(0);
        editText.setText(stringBuilder.toString());
    }
}
