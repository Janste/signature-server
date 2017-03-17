Handwritten Signature SDK by Luciano Giuseppe 
based on http://code.google.com/p/signature-verification/
Last edit: Jan 2014
License: MIT 

Features:
*Dynamic time warping (DTW) with sakoe chiba band
*Extended Regression in 2 dimensions
*Signature sampling by points coordinates
*Directional Hash: generate an hash from signature according to X and Y movement (a my idea)
*Methods to extract: velocity, pressure, internal and external angles and critical points


Legend: * fixed, + added, - removed

Release 0.6.1: (thx to Roscigno Gianluca [http://www.di.unisa.it/dottorandi/roscigno/])  
*Signaturesdk.features.Extract.calculateVelocities: removed absolute value from division, now critical points extraction works correctly.
*Signaturesdk.features.Extract.totalTime: (copy&paste error) now compute the correct total time.
 
Release 0.6:
+Signauresdk.storage.XML: import/export points in XML (using java SAX)
+Signauresdk.storage.CSV: import/export points in CSV (to use with spreadsheet editor)
These classes are useful to make signature analysis

Release 0.5.5:
+Signaturesdk.hash.Directional improved
*Signaturesdk.features.Sample.sampleWord fixed
*Signatursdk.acquisition.SignatuteJPanel.drawNormalizedSignature fixed

Release 0.5:
+Added storage and acquisition classes for Android device

Release 0.4.1:
*features.Normalize.merger(): I forgot to merge pressure

Release 0.4:
+Pressure support (thanks to JPen library)
*acquisition.SignatureJPanel & features.Extract: time is taken in nano seconds, to work fine with velocity
+SignatureJPanel: added hightQuality(boolean) to have a good signature rendering quality
*features.Extract: calculateVelocities() fixed distance calculation and throws an exception where there's a delta time zero
+features.utils.GMath optimized
+verification.Verification.pressionDTW()

Release 0.3:
*features.Normalize.java: merger() fixed!
*verification.DTW.java optimized code
*verification.Verification.java modified
*storage.Disk.java optimized code
+beans.SignWord: it can import AcquisitionSingWord cloning lists or copying references
*hash.Directional: compute() method fixed!!
+features.Sample: now can sample a word considering word parts or just as one
+beans.SignWord: in constructors it can clone lists or copies lists reference 

Release 0.2:
+Acquisition points separated from normalized points: all methods(DTW, ER2, etc) use only normalized points
*features.Normalize.java: size() and coords() methods
+features.Normalize.java: words rotation (in testing) and merging
+hash.Directional.java: improved directional hash algorithm