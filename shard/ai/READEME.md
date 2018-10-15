# AI

AI is at the heart of Shard. It controls how the guests, and murderer, behave
and interact with their environment.

Each Guest has several Traits that determine their behaviour. Each trait
provides a modifier that is then used when determining the action that the
Guest will take.

For example, the SUSPICOUS trait will cause Guests to sometimes lie to the
player when asked about what they've seen. The murderer always has this trait.
(This is implemented by creating false memories).