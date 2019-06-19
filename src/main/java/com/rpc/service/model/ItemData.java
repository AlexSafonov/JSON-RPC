package com.rpc.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemData {

    private String id_sample;     // - идетнификатор предмета(76rtw)
    private String num_id;        // -серийный номер предмета(ffg#er111)
    private String id_location;   // -место откуда(3211.2334), а может быть(Екатеринбург)
    private String id_signal_par; // -датчик, с которого идет сигнал(0xcv11cs)
    private String id_detected;   // - данные о состоянии (None), - исправен, (Nan), - поломка
    private String id_class_det;  // - вид поломки(req11).

}
