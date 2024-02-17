| Command                          | Permission       |
|----------------------------------|------------------|
| `/tp @destination`                 | basics.tp        |
| `/tp <x y z [pitch yaw]>`          | basics.tp        |
| `/tp @targets @destination`        | basics.tp.others |
| `/tp @targets <x y z [pitch yaw]>` | basics.tp.others |


> [!TIP]  
> Coordinates **relative to the sender** can be used with `~x ~y ~z ~pitch ~yaw`
> 
> Coordinates **relative to the target(s)** can be used with `~~x ~~y ~~z ~~pitch ~~yaw`
> 
> Examples:
> - `/tp @e[type=pig] ~ ~ ~5` Teleports all pigs 5 blocks south of **the sender's** position
> - `/tp @e[type=pig] ~~ ~~ ~~1 ` Teleports all pigs 1 block south from **their current** position


> [!NOTE]  
> Using selectors additionally requires permission `basics.selectors`.
