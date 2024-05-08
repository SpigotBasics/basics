| Command                  | Permission       |
|--------------------------|------------------|
| `/chatcolor set <color>` | basics.chatcolor |
| `/chatcolor reset`       | basics.chatcolor |

## Color Permissions

Color permissions are given based upon certain permissions. Below will explain how you can grant permission
to specific colors alongside ranges of colors.

### Giving Ranges

There are a few ranges provided in basic by default.

- `basics.chatcolor.named`, gives access to all named colors, e.g. red, blue, dark_green.
- `basics.chatcolor.hex`, gives access to all hex colors, e.g. #FF00FF, #00FF00, #EFABCE.
- `basics.chatcolor.gradient.named`, gives access to any gradient that is named, e.g. red:blue, gold:light_purple, yellow:green.
- `basics.chatcolor.gradient.hex`, gives access to any hex that is named, e.g. #FFF000:#000FFF, #EEFF00:#00FFEE.

### Giving Specific Colors

If you don't want to give players ranges, but rather specific colors you can do the following. An example will be given for each type

- **Named**: `basics.chatcolor.red`
- **Hex**: `basics.chatcolor.#987FFE`
- **Named Gradient**: `basics.chatcolor.blue:red`
- **Hex Gradient**: `basics.chatcolor.#FFF000:#000FFF`
