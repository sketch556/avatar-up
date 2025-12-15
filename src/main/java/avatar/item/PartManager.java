package avatar.item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import avatar.db.DbManager;
import lombok.Getter;

public class PartManager {

    private static final PartManager instance = new PartManager();

    public static PartManager getInstance() {
        return instance;
    }

    @Getter
    private final List<Part> parts = new ArrayList<>();

    public void load() {
        try {
            parts.clear();
            PreparedStatement ps = DbManager.getInstance().getConnectionForGame()
                    .prepareStatement("SELECT * FROM `items`;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int coin = rs.getInt("coin");
                int gold = rs.getInt("gold");
                short type = rs.getShort("type");
                String name = rs.getString("name");
                short icon = rs.getShort("icon");
                int expiredDay = rs.getInt("expired_day");
                byte level = rs.getByte("level");
                byte sell = rs.getByte("sell");
                byte zOrder = rs.getByte("zorder");
                short[] imgID = new short[15];
                byte[] dx = new byte[15];
                byte[] dy = new byte[15];
                JSONArray animation = (JSONArray) JSONValue.parse(rs.getString("animation"));
                int size = animation.size();
                for (int i = 0; i < size; i++) {
                    JSONObject obj = (JSONObject) animation.get(i);
                    imgID[i] = ((Long) obj.get("img")).shortValue();
                    dx[i] = ((Long) obj.get("dx")).byteValue();
                    dy[i] = ((Long) obj.get("dy")).byteValue();
                }
                parts.add(Part.builder().id(id)
                        .coin(coin)
                        .gold(gold)
                        .type(type)
                        .name(name)
                        .icon(icon)
                        .expiredDay(expiredDay)
                        .level(level)
                        .sell(sell)
                        .zOrder(zOrder)
                        .imgID(imgID)
                        .dx(dx)
                        .dy(dy)
                        .build());
                System.out.println("id: " + id + " name: " + name);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Part> getAvatarPart() {
        return parts.stream().filter((t) -> t.getId() < 2000).toList();
    }

    public Part findPartByID(int id) {
        for (Part part : parts) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

}
