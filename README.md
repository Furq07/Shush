# 🤫 **Shush**

Allows players to mute other players' messages for themselves, providing a personalized chat experience.

---

## 🎧 **Key Features**

| Feature | Description |
| --- | --- |
| **🔇 Personal Muting** | Players can mute specific users' messages for themselves. |
| **💬 Chat Filtering** | Automatically filters muted players' messages from chat. |
| **📨 Private Message Control** | Prevents muted players from sending private messages. |
| **💡 User-Friendly** | Simple commands for muting and unmuting players. |
| **⚙ Configurable** | Easily customize messages through the `config.yml` file. |
| **🔃 Cross-Version Compatibility** | Supports Minecraft versions from `1.8` to the latest. |

---

## 💻 **Available Commands**

| Command | Description |
| --- | --- |
| **`/shush <player>`** | Toggle mute status for a specific player. |
| **`/shush reload`** | Reload the plugin's configuration. |

---

## 🔒 **Permissions**

| Permission | Description |
| --- | --- |
| **`shush.reload`** | Allows reloading of the plugin configuration. |
| **`shush.bypass`** | Allows bypassing the mute system. Players with this permission can't be muted. |

---

## 📩 **Installation Steps**

1. **Download Shush:**
   - Get the latest version from [Modrinth](https://modrinth.com/plugin/shush).

2. **Install:**
   - Place the `.jar` file into your server's `plugins` directory.
   - Example: `/plugins/Shush-x.x.jar`

3. **Activate:**
   - Restart your server or use a plugin manager to load Shush.

4. **Customize:**
   - Edit the `config.yml` file located in `plugins/Shush/config.yml` to customize messages.
   - Example configuration:
     ```yaml
     prefix: "&7[&bShush&7]"
     messages:
       muted: "&aYou have muted {player}'s messages."
       unmuted: "&aYou have unmuted {player}'s messages."
       cannot_mute_bypass: "&cYou cannot mute this player as they have bypass permission."
     ```

---

## 🎮 **Usage**

1. **Mute a Player:**
   - Use `/shush <player>` to toggle mute status for a specific player.
   - Example: `/shush Furq_`

2. **Reload Configuration:**
   - Use `/shush reload` to reload the plugin's configuration (requires permission).

3. **Private Messaging:**
   - Private messages using `/msg`, `/tell`, or `/w` are monitored and blocked if the sender has been muted by the recipient.

---

## 📞 **Support**

For assistance, visit the [GitHub Repository](https://github.com/furq07/shush/issues) or join our [Discord Server](https://discord.gg/XhZzmvzPDV).

---

## 📜 **License**

Shush is released under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
