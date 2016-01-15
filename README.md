# NeoWiSARD
Weightless Neural Network project.

First steps include developing a piece of software capable of creating several instances of a WiSARD model and transfer knowledge between two WiSARD instances trained, each one, with a different datasets about the same problem.

#A bit about WiSARD

WiSARD is a Weightless Neural Network model created by Wilkie, Stonham and Aleksander in 1984. It's also called a multidiscriminator RAM-based model.

Basic WiSARD takes a binary x by y input - referred as retina - and creates a pseudo-random mapping between a set of n pixels - or n-tuples - and an address of a RAM memory. The address if formed from the concatanation of the values stored in the pixels.

WiSARD's main advantages are the extremely fast training phase and the simple representation used by the model.

#A bit about DRASiW

DRASiW is an adaptation of the WiSARD model that is capable of generating what can be considered as the WiSARD's mental images. What it does is to, given the label of a learned pattern, pick all information stored in the correspondent discriminator's RAMs and produce a gray-scale image based on that information.

#A bit about Transfer Learning

Consider a situation where a person faces a problem similar to another problem it has previously faced. To solve the current problem, the person takes the expecience obtained from the old problem and tries to aplly it. In other words, the person has transfered what was learned in one context to another.
