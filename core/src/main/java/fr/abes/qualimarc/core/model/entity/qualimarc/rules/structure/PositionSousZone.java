package fr.abes.qualimarc.core.model.entity.qualimarc.rules.structure;

import fr.abes.qualimarc.core.model.entity.notice.Datafield;
import fr.abes.qualimarc.core.model.entity.notice.NoticeXml;
import fr.abes.qualimarc.core.model.entity.qualimarc.reference.FamilleDocument;
import fr.abes.qualimarc.core.model.entity.qualimarc.rules.Rule;
import fr.abes.qualimarc.core.utils.Priority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "RULE_POSITIONSOUSZONE")
public class PositionSousZone extends Rule implements Serializable {
    @Column(name = "SOUS_ZONE")
    private String sousZone;
    @Column(name = "POSITION")
    private Integer position;

    public PositionSousZone(Integer id, String message, String zone, String sousZone, Priority priority, Integer position) {
        super(id, message, zone, priority);
        this.sousZone = sousZone;
        this.position = position;
    }

    public PositionSousZone(Integer id, String message, String zone, Priority priority, Set<FamilleDocument> familleDocuments, String sousZone, Integer position) {
        super(id, message, zone, priority, familleDocuments);
        this.sousZone = sousZone;
        this.position = position;
    }

    @Override
    public boolean isValid(NoticeXml notice) {
        //récupération de toutes les zones définies dans la règle
        List<Datafield> zones = notice.getDatafields().stream().filter(datafield -> datafield.getTag().equals(this.getZone())).collect(Collectors.toList());
        //vérification pour chaque zone répétée
        for (Datafield zone : zones) {
            //si la position de la sous zone n'est pas la position de la règle, la règle n'est pas valide
            if (zone.getSubFields().stream().map(sf -> sf.getCode()).collect(Collectors.toList()).indexOf(sousZone) != (position -1)) {
                return true;
            }
        }
        return false;
    }
}
